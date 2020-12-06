package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import core.boundary.SnippetIOBoundary;
import core.boundary.UserIOBoundary;
import core.mock.MockSnippetRepository;
import core.mock.MockUserRepository;
import core.usecase.snippet.CreateSnippet;
import core.usecase.snippet.RetrievePublicSnippet;
import core.usecase.snippet.SnippetInteractorManager;
import core.usecase.snippet.UpdateSnippet;
import core.usecase.snippet.AbstractSnippetInteractor.DifferentSnippetOwnerException;
import core.usecase.snippet.AbstractSnippetInteractor.HiddenSnippetException;
import core.usecase.snippet.AbstractSnippetInteractor.NoSuchSnippetException;
import core.usecase.user.CreateUser;
import core.usecase.user.UserInteractorManager;

@TestInstance(Lifecycle.PER_CLASS)
public class SnippetInteractorTest {

    UserIOBoundary userInteractor;
    SnippetIOBoundary snippetInteractor;

    @BeforeAll
    public void setUp() {
        var userDb = new MockUserRepository();
        var snippetDb = new MockSnippetRepository();
        snippetInteractor = new SnippetInteractorManager(snippetDb, userDb);
        userInteractor = new UserInteractorManager(userDb);
        createUsers();
        createSnippets();
    }

    void createUsers() {
        userInteractor.createUser(new CreateUser.RequestModel("noah", "asdlfkwe", "a@g.com"));
        userInteractor.createUser(new CreateUser.RequestModel("kate", "asdlfkwe", "a@g.com"));
        userInteractor.createUser(new CreateUser.RequestModel("norbe", "asdlfkwe", "a@g.com"));
    }

    void createSnippets() {
        var request = CreateSnippet.RequestModel.builder().title("test").language("java").framework(null).code("assert")
                .description("rigor").resource(null).owner("noah").hidden(false)
                .whenCreated(LocalDateTime.of(2020, 12, 5, 0, 0, 0))
                .whenLastModified(LocalDateTime.of(2020, 12, 5, 0, 0, 0)).build();
        var expected = new CreateSnippet.ResponseModel("Snippet has been successfully saved.");
        var actual = snippetInteractor.createSnippet(request);

        assertEquals(expected, actual);

        var request1 = CreateSnippet.RequestModel.builder().title("test").language("java").framework(null)
                .code("assert").description("rigor").resource(null).owner("kate").hidden(true)
                .whenCreated(LocalDateTime.of(2020, 12, 5, 0, 0, 0))
                .whenLastModified(LocalDateTime.of(2020, 12, 5, 0, 0, 0)).build();
        var expected1 = new CreateSnippet.ResponseModel("Snippet has been successfully saved.");
        var actual1 = snippetInteractor.createSnippet(request1);

        assertEquals(expected1, actual1);

    }

    @Test
    void createSnippetWithBadRequests() {
        var requestWithNullTitle = CreateSnippet.RequestModel.builder().title(null).language("java").framework(null)
                .code("assert").description("rigor").resource(null).owner("noah").hidden(false)
                .whenCreated(LocalDateTime.of(2020, 12, 5, 0, 0, 0))
                .whenLastModified(LocalDateTime.of(2020, 12, 5, 0, 0, 0)).build();

        assertThrows(NullPointerException.class, () -> snippetInteractor.createSnippet(requestWithNullTitle));

        var requestWithNullCode = CreateSnippet.RequestModel.builder().title("").language("java").framework(null)
                .code(null).description("rigor").resource(null).owner("noah").hidden(false)
                .whenCreated(LocalDateTime.of(2020, 12, 5, 0, 0, 0))
                .whenLastModified(LocalDateTime.of(2020, 12, 5, 0, 0, 0)).build();

        assertThrows(NullPointerException.class, () -> snippetInteractor.createSnippet(requestWithNullCode));

        var requestWithNullDateModified = CreateSnippet.RequestModel.builder().title("").language("java")
                .framework(null).code(null).description("rigor").resource(null).owner("noah").hidden(false)
                .whenCreated(LocalDateTime.of(2020, 12, 5, 0, 0, 0)).whenLastModified(null).build();

        assertThrows(NullPointerException.class, () -> snippetInteractor.createSnippet(requestWithNullDateModified));
    }

    @Test
    void updateSnippet() {
        var request = UpdateSnippet.RequestModel.builder().id(1l).title("test").language("js").code("expectToBe")
                .description("rigor").owner("noah").whenCreated(LocalDateTime.of(2020, 12, 5, 0, 0, 0))
                .whenLastModified(LocalDateTime.of(2020, 12, 5, 0, 0, 0)).build();
        var expected = new UpdateSnippet.ResponseModel("Snippet has been successfully updated.");
        var actual = snippetInteractor.updateSnippet(request);
        assertEquals(expected, actual);

        var request1 = new RetrievePublicSnippet.RequestModel(1);
        var previous = new RetrievePublicSnippet.ResponseModel("test", "java", null, "assert", "rigor", null, "noah",
                false, "2020-12-05 00:00", "2020-12-05 00:00");

        previous = RetrievePublicSnippet.ResponseModel.builder().title("test").language("java").framework(null)
                .code("assert").description("rigor").resource(null).owner("noah").hidden(false)
                .whenCreated("2020-12-05 00:00").whenLastModified("2020-12-05 00:00").build();

        var actualSnippet = snippetInteractor.retrieveSnippet(request1);
        assertNotEquals(previous, actualSnippet);

        var current = RetrievePublicSnippet.ResponseModel.builder().title("test").language("js").code("expectToBe")
                .description("rigor").owner("noah").whenCreated("2020-12-05 00:00").whenLastModified("2020-12-05 00:00")
                .build();
        assertEquals(current, actualSnippet);
    }

    @Test
    void updateSnippetOfOtherUserThrowsException() {
        var requestFromJamesNotNoah = UpdateSnippet.RequestModel.builder().id(1l).title("test").language("js")
                .code("expectToBe").description("rigor").owner("james")
                .whenCreated(LocalDateTime.of(2020, 12, 5, 0, 0, 0))
                .whenLastModified(LocalDateTime.of(2020, 12, 5, 0, 0, 0)).build();

        assertThrows(DifferentSnippetOwnerException.class,
                () -> snippetInteractor.updateSnippet(requestFromJamesNotNoah));
    }

    @ParameterizedTest
    @ValueSource(longs = { 1 })
    void getASnippet(long id) {
        var request = new RetrievePublicSnippet.RequestModel(id);
        var expected = RetrievePublicSnippet.ResponseModel.builder().title("test").language("java").framework(null)
                .code("assert").description("rigor").resource(null).owner("noah").hidden(false)
                .whenCreated("2020-12-05 00:00").whenLastModified("2020-12-05 00:00").build();
        var actual = snippetInteractor.retrieveSnippet(request);
        assertEquals(expected, actual);
    }

    @Test
    void getNonExistentSnippetThrowsException() {
        var request = new RetrievePublicSnippet.RequestModel(9);
        assertThrows(NoSuchSnippetException.class, () -> snippetInteractor.retrieveSnippet(request));
    }

    @Test
    void getSnippetButIsHiddenSoThrowException() {
        var request = new RetrievePublicSnippet.RequestModel(2);
        assertThrows(HiddenSnippetException.class, () -> snippetInteractor.retrieveSnippet(request));
    }

}
