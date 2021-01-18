package core;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import core.boundary.SnippetIOBoundary;
import core.boundary.UserIOBoundary;
import core.dto.SnippetDTO;
import core.dto.UserDTO;
import core.gateway.SnippetGateway;
import core.gateway.UserGateway;
import core.mock.MockActivationTokenRepo;
import core.mock.MockSnippetRepository;
import core.mock.MockUserRepository;
import core.usecase.UseCaseException.DifferentSnippetOwnerException;
import core.usecase.UseCaseException.HiddenSnippetException;
import core.usecase.UseCaseException.NoSuchSnippetException;
import core.usecase.snippet.CreateSnippet;
import core.usecase.snippet.DeleteSnippetOfUser;
import core.usecase.snippet.RetrieveAllPublicSnippets;
import core.usecase.snippet.RetrieveAllSnippetsOfUser;
import core.usecase.snippet.RetrievePublicSnippet;
import core.usecase.snippet.RetrieveRecentSnippets;
import core.usecase.snippet.RetrieveSnippetOfUser;
import core.usecase.snippet.SearchPublicSnippets;
import core.usecase.snippet.SnippetInteractorManager;
import core.usecase.snippet.UpdateSnippet;
import core.usecase.snippet.SearchPublicSnippets.Mode;
import core.usecase.user.UserInteractorManager;
import core.utils.Page;

public class SnippetInteractorTest {

    static UserIOBoundary userInteractor;
    static SnippetIOBoundary snippetInteractor;
    static DummnyOwnedSnippets dummyOwnedSnippets;
    static UserGateway userDb;
    static Runnable repoCleaner;
    static SnippetGateway snippetDb;

    @BeforeAll
    public static void setUp() {
        userDb = new MockUserRepository();
        snippetDb = new MockSnippetRepository();
        repoCleaner = () -> ((MockSnippetRepository) snippetDb).clearRepo();
        snippetInteractor = new SnippetInteractorManager(snippetDb, userDb);
        userInteractor = new UserInteractorManager(userDb, new MockActivationTokenRepo());
        dummyOwnedSnippets = new DummnyOwnedSnippets(snippetInteractor, userDb.findAll());
    }


    @BeforeEach
    void init() {
        dummyOwnedSnippets.createSnippets(snippetDto -> {
            var request = new CreateSnippet.RequestModel(snippetDto);
            var expected = new CreateSnippet.ResponseModel("Snippet has been successfully saved.");
            var actual = snippetInteractor.createSnippet(request);

            assertEquals(expected, actual);
        });
    }

    @AfterEach
    void cleanUp() {
        repoCleaner.run();
    }

    @Test
    void createSnippetWithBadRequests() {
        UserDTO noah = userDb.findByUserName("noah").get().toUserDto();

        assertThrows(NullPointerException.class, () -> {
            var requestWithNullTitle = new CreateSnippet.RequestModel(
                    SnippetDTO.builder().title(null).language("java").framework(null).code("assert")
                            .description("rigor").resource(null).owner(noah).hidden(false)
                            .whenCreated(LocalDateTime.of(2020, 12, 5, 0, 0, 0))
                            .whenLastModified(LocalDateTime.of(2020, 12, 5, 0, 0, 0)).build());
            snippetInteractor.createSnippet(requestWithNullTitle);
        });

        assertThrows(NullPointerException.class, () -> {
            var requestWithNullCode = new CreateSnippet.RequestModel(SnippetDTO.builder().title("")
                    .language("java").framework(null).code(null).description("rigor").resource(null)
                    .owner(noah).hidden(false).whenCreated(LocalDateTime.of(2020, 12, 5, 0, 0, 0))
                    .whenLastModified(LocalDateTime.of(2020, 12, 5, 0, 0, 0)).build());
            snippetInteractor.createSnippet(requestWithNullCode);
        });

        assertThrows(NullPointerException.class, () -> {
            var requestWithNullDateModified = new CreateSnippet.RequestModel(
                    SnippetDTO.builder().title("").language("java").framework(null).code(null)
                            .description("rigor").resource(null).owner(noah).hidden(false)
                            .whenCreated(LocalDateTime.of(2020, 12, 5, 0, 0, 0))
                            .whenLastModified(null).build());
            snippetInteractor.createSnippet(requestWithNullDateModified);
        });
    }

    @Test
    void updateSnippet() {
        UserDTO noah = userDb.findByUserName("noah").get().toUserDto();
        SnippetDTO moddedSnippet = SnippetDTO.builder().id(1l).title("test").language("js")
                .code("expectToBe").description("rigor").owner(noah)
                .whenCreated(LocalDateTime.of(2020, 12, 5, 0, 0, 0))
                .whenLastModified(LocalDateTime.of(2020, 12, 5, 0, 0, 0)).build();

        var requestWithModifiedSnippet = new UpdateSnippet.RequestModel(moddedSnippet);
        var expected = new UpdateSnippet.ResponseModel("Snippet has been successfully updated.");
        var actual = snippetInteractor.updateSnippet(requestWithModifiedSnippet);

        assertEquals(expected, actual);

        var requestSameSnippet = new RetrievePublicSnippet.RequestModel(1);

        var previous = new RetrievePublicSnippet.ResponseModel(
                dummyOwnedSnippets.getSnippetOfUser("noah"));

        var actualSnippet = snippetInteractor.retrievePublicSnippet(requestSameSnippet);
        assertNotEquals(previous, actualSnippet);

        var shouldBe = new RetrievePublicSnippet.ResponseModel(moddedSnippet);
        assertEquals(shouldBe, actualSnippet);
    }

    @Test
    void updateSnippetOfOtherUserThrowsException() {
        UserDTO james = userDb.findByUserName("james").get().toUserDto();
        var requestFromJamesNotNoah = new UpdateSnippet.RequestModel(SnippetDTO.builder().id(1l)
                .title("test").language("js").code("expectToBe").description("rigor").owner(james)
                .whenCreated(LocalDateTime.of(2020, 12, 5, 0, 0, 0))
                .whenLastModified(LocalDateTime.of(2020, 12, 5, 0, 0, 0)).build());

        assertThrows(DifferentSnippetOwnerException.class,
                () -> snippetInteractor.updateSnippet(requestFromJamesNotNoah));
    }

    @ParameterizedTest
    @ValueSource(longs = {1})
    void getASnippet(long id) {
        String username = userDb.findById(id).get().getUsername();
        var request = new RetrievePublicSnippet.RequestModel(id);
        var expected = new RetrievePublicSnippet.ResponseModel(
                dummyOwnedSnippets.getSnippetOfUser(username));
        var actual = snippetInteractor.retrievePublicSnippet(request);
        assertEquals(expected, actual);
    }

    @Test
    void getNonExistentSnippetThrowsException() {
        var request = new RetrievePublicSnippet.RequestModel(9);
        assertThrows(NoSuchSnippetException.class,
                () -> snippetInteractor.retrievePublicSnippet(request));
    }

    @Test
    void getHiddenSnippetOfSomeoneElseThrowException() {
        var request = new RetrievePublicSnippet.RequestModel(2);
        assertThrows(HiddenSnippetException.class,
                () -> snippetInteractor.retrievePublicSnippet(request));
    }

    @Test
    void userGetsHisOwnSnippet() {
        var request = new RetrieveSnippetOfUser.RequestModel(2l, "kate");
        var expected = new RetrieveSnippetOfUser.ResponseModel(
                dummyOwnedSnippets.getSnippetOfUser("kate"));
        assertEquals(expected, snippetInteractor.retrieveSnippetOfUser(request));
    }

    @Test
    void userGetsSnippetOfDifferentUserThrowsException() {
        var request = new RetrieveSnippetOfUser.RequestModel(2l, "noah");
        assertThrows(DifferentSnippetOwnerException.class,
                () -> snippetInteractor.retrieveSnippetOfUser(request));
    }

    @Test
    void getAllPublicSnippets() {
        int page = 1;
        int pageSize;
        for (pageSize = 5; pageSize > 0; pageSize--) {
            var request = new RetrieveAllPublicSnippets.RequestModel(page, pageSize);
            RetrieveAllPublicSnippets.ResponseModel response =
                    snippetInteractor.retrieveAllPublicSnippets(request);
            assertEquals(pageSize, response.getPage().getSize());
            var expected = dummyOwnedSnippets.getOwnerSnippetMap().values().stream()
                    .filter(s -> !s.isHidden()).limit(pageSize).collect(Collectors.toList());
            assertIterableEquals(expected, response.getPage().getContent());
        }

        page = 2;
        var request = new RetrieveAllPublicSnippets.RequestModel(page, pageSize);
        var response = snippetInteractor.retrieveAllPublicSnippets(request);
        assertIterableEquals(List.of(), response.getPage().getContent());
    }

    @Test
    void getMostRecentSnippets() {
        var request = new RetrieveRecentSnippets.RequestModel(3);
        RetrieveRecentSnippets.ResponseModel response =
                snippetInteractor.retrieveRecentSnippets(request);
        var recentSnippets = response.getSnippets();

        assertEquals(3, recentSnippets.size());
        assertIterableEquals(dummyOwnedSnippets.getOwnerSnippetMap().values().stream()
                .sorted((x, y) -> y.getWhenLastModified().compareTo(x.getWhenLastModified()))
                .limit(3).collect(Collectors.toList()), recentSnippets);
    }

    @ParameterizedTest
    @CsvSource(value = {"true, 2", "false, 5"})
    public void getAllSnippetsOfUser(boolean hidden, int pageSize) {
        UserDTO owner = userDb.findAll().stream().findAny().get().toUserDto();
        Collection<SnippetDTO> dummySnippets =
                dummyOwnedSnippets.createManyDummySnippetsOwnedBy(owner, hidden, pageSize);
        var request = new RetrieveAllSnippetsOfUser.RequestModel(owner.getUsername(), 1, pageSize);

        var page = new Page<SnippetDTO>(1, 0, pageSize,
                dummySnippets.stream().limit(pageSize).collect(Collectors.toList()));
        var expectedResponse = new RetrieveAllSnippetsOfUser.ResponseModel(page);

        var actualResponse = snippetInteractor.retrieveSnippetsOfUser(request);
        assertEquals(expectedResponse.getPage().getSize(), actualResponse.getPage().getSize());
        assertIterableEquals(expectedResponse.getPage().getContent(),
                actualResponse.getPage().getContent());

        var requestSecondPage =
                new RetrieveAllSnippetsOfUser.RequestModel(owner.getUsername(), 3, pageSize);
        expectedResponse = new RetrieveAllSnippetsOfUser.ResponseModel(
                new Page<SnippetDTO>(1, 0, pageSize, List.of()));


        assertEquals(expectedResponse.getPage().getSize(), actualResponse.getPage().getSize());
        actualResponse = snippetInteractor.retrieveSnippetsOfUser(requestSecondPage);
        assertIterableEquals(expectedResponse.getPage().getContent(),
                actualResponse.getPage().getContent());

    }

    @Test
    public void deleteSnippetOfUserTest() {
        long snippetId = 5;
        var response = snippetInteractor.deleteOne(new DeleteSnippetOfUser.RequestModel(snippetId));

        assertEquals("Snippet was deleted successfully.", response.getMessage());

        var request = new RetrievePublicSnippet.RequestModel(snippetId);
        assertThrows(NoSuchSnippetException.class,
                () -> snippetInteractor.retrievePublicSnippet(request));
    }

    @ParameterizedTest
    @CsvSource(value = {"java, SIMPLE, 0, 2", "jav., REGEX, -8, 2", "jav.*, REGEX, 1, 0"})
    public void searchPublicSnippetsTest(String phrase, String mode, int pageNum, int pageSize) {
        var request = new SearchPublicSnippets.RequestModel(phrase, Mode.valueOf(mode), pageNum,
                pageSize);
        var response = snippetInteractor.searchPublicSnippets(request);
        var page = response.getPage();

        pageSize = pageSize < 1 ? 1 : pageSize;
        pageNum = pageNum < 0 ? 0 : pageNum;
        assertEquals(pageNum, page.getNumber());
        assertEquals(pageSize, page.getSize());
        assertEquals(snippetDb.count() / pageSize, page.getTotal());
        assertFalse(page.getContent().isEmpty());

        long hiddenSnippetsNum = page.getContent().stream().filter(SnippetDTO::isHidden).count();
        assertEquals(0, hiddenSnippetsNum);

        List<SnippetDTO> content = new ArrayList<>(page.getContent());
        assertTrue(content.size() <= pageSize);
        boolean lastSnippetInContentIsNotFirstInDb = content.get(content.size() - 1).getId() != 1;
        assertTrue(lastSnippetInContentIsNotFirstInDb);
    }

    @ParameterizedTest
    @CsvSource(value = {"SIMPLE, 0, 0", "REGEX, -8, -2"})
    void searchPublicSnippetsTestValidateRequest(String mode, int pageNum, int pageSize){

        var request = new SearchPublicSnippets.RequestModel("java", Mode.valueOf(mode), pageNum,
                pageSize);
        var response = snippetInteractor.searchPublicSnippets(request);
        var page = response.getPage();

        pageNum = 0; pageSize = 1;
        assertEquals(pageNum, page.getNumber());
        assertEquals(pageSize, page.getSize());
        assertEquals(snippetDb.count() / pageSize, page.getTotal());
        assertFalse(page.getContent().isEmpty());

        long hiddenSnippetsNum = page.getContent().stream().filter(SnippetDTO::isHidden).count();
        assertEquals(0, hiddenSnippetsNum);

        List<SnippetDTO> content = new ArrayList<>(page.getContent());
        assertTrue(content.size() <= pageSize);
    }

}
