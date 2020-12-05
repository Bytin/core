package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import core.boundary.SnippetIOBoundary;
import core.boundary.UserIOBoundary;
import core.mock.MockSnippetRepository;
import core.mock.MockUserRepository;
import core.usecase.snippet.CreateSnippet;
import core.usecase.snippet.RetrievePublicSnippet;
import core.usecase.snippet.SnippetInteractorManager;
import core.usecase.snippet.AbstractSnippetInteractor.HiddenSnippetException;
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
        var request = new CreateSnippet.RequestModel("test", "java", null, "assert", "rigor", null, "noah", false,
                LocalDateTime.of(2020, 12, 5, 0, 0, 0), LocalDateTime.of(2020, 12, 5, 0, 0, 0));
        var expected = new CreateSnippet.ResponseModel("Snippet has been successfully saved.");
        var actual = snippetInteractor.createSnippet(request);
        assertEquals(expected, actual);

        var request1 = new CreateSnippet.RequestModel("test", "java", null, "assert", "rigor", null, "kate", true,
                LocalDateTime.of(2020, 12, 5, 0, 0, 0), LocalDateTime.of(2020, 12, 5, 0, 0, 0));
        var expected1 = new CreateSnippet.ResponseModel("Snippet has been successfully saved.");
        var actual1 = snippetInteractor.createSnippet(request1);
        assertEquals(expected1, actual1);
 
    }

    @Test
    void createSnippetWithBadRequests() {
        var request = new CreateSnippet.RequestModel(null, "java", null, "assert", "rigor", null, "noah", false,
                LocalDateTime.of(2020, 12, 5, 0, 0, 0), LocalDateTime.of(2020, 12, 5, 0, 0, 0));
        assertThrows(NullPointerException.class, () -> snippetInteractor.createSnippet(request));

        var request1 = new CreateSnippet.RequestModel("", "java", null, null, "rigor", null, "noah", false,
                LocalDateTime.of(2020, 12, 5, 0, 0, 0), LocalDateTime.of(2020, 12, 5, 0, 0, 0));
        assertThrows(NullPointerException.class, () -> snippetInteractor.createSnippet(request1));

        var request2 = new CreateSnippet.RequestModel("", "java", null, "assert", "rigor", null, "noah", true,
                LocalDateTime.of(2020, 12, 5, 0, 0, 0), null);
        assertThrows(NullPointerException.class, () -> snippetInteractor.createSnippet(request2));
    }

    @Test
    void getASnippet() {
        var request = new RetrievePublicSnippet.RequestModel(1);
        var expected = new RetrievePublicSnippet.ResponseModel("test", "java", null, "assert", "rigor", null, "noah", false,
                "2020-12-05 00:00", "2020-12-05 00:00");
        var actual = snippetInteractor.retrieveSnippet(request);
        assertEquals(expected, actual);
    }

    @Test
    void getSnippetButIsHiddenSoThrowException(){
        var request = new RetrievePublicSnippet.RequestModel(2);
        assertThrows(HiddenSnippetException.class, () -> snippetInteractor.retrieveSnippet(request));
    }

}
