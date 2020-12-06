package core;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import core.boundary.*;
import core.dto.SnippetDTO;
import core.mock.*;
import core.usecase.snippet.*;
import core.usecase.snippet.AbstractSnippetInteractor.*;
import core.usecase.user.*;

@TestInstance(Lifecycle.PER_CLASS)
public class SnippetInteractorTest {

        UserIOBoundary userInteractor;
        SnippetIOBoundary snippetInteractor;
        String[] snippetOwners;
        Map<String, SnippetDTO> ownerSnippetMap;

        @BeforeAll
        public void setUp() {
                var userDb = new MockUserRepository();
                var snippetDb = new MockSnippetRepository();
                snippetInteractor = new SnippetInteractorManager(snippetDb, userDb);
                userInteractor = new UserInteractorManager(userDb);
                snippetOwners = new String[] {"noah", "kate", "andy", "alice", "adam"};
                ownerSnippetMap = new HashMap<>();
                createUsers();
                createSnippets();
        }

        void createUsers() {
                for (String owner : snippetOwners) {
                        userInteractor.createUser(
                                        new CreateUser.RequestModel(owner, "asdlfkwe", "a@g.com"));
                }
        }

        void createSnippets() {
                boolean[] hidden = new boolean[] {false, true, true, false, false};

                for (int i = 0; i < snippetOwners.length; i++) {
                        SnippetDTO snippetDTO = SnippetDTO.builder().id(i + 1).title("test")
                                        .language("java").framework(null).code("assert")
                                        .description("rigor").resource(null).owner(snippetOwners[i])
                                        .hidden(hidden[i])
                                        .whenCreated(LocalDateTime.of(2020, 12, 5, i, 0, 0))
                                        .whenLastModified(LocalDateTime.of(2020, 12, 5, i, 0, 0))
                                        .build();

                        var request = new CreateSnippet.RequestModel(snippetDTO);
                        var expected = new CreateSnippet.ResponseModel(
                                        "Snippet has been successfully saved.");
                        var actual = snippetInteractor.createSnippet(request);

                        assertEquals(expected, actual);

                        ownerSnippetMap.put(snippetOwners[i], snippetDTO);
                }

        }

        @Test
        void createSnippetWithBadRequests() {
                var requestWithNullTitle = new CreateSnippet.RequestModel(SnippetDTO.builder()
                                .title(null).language("java").framework(null).code("assert")
                                .description("rigor").resource(null).owner("noah").hidden(false)
                                .whenCreated(LocalDateTime.of(2020, 12, 5, 0, 0, 0))
                                .whenLastModified(LocalDateTime.of(2020, 12, 5, 0, 0, 0)).build());

                assertThrows(NullPointerException.class,
                                () -> snippetInteractor.createSnippet(requestWithNullTitle));

                var requestWithNullCode = new CreateSnippet.RequestModel(SnippetDTO.builder()
                                .title("").language("java").framework(null).code(null)
                                .description("rigor").resource(null).owner("noah").hidden(false)
                                .whenCreated(LocalDateTime.of(2020, 12, 5, 0, 0, 0))
                                .whenLastModified(LocalDateTime.of(2020, 12, 5, 0, 0, 0)).build());

                assertThrows(NullPointerException.class,
                                () -> snippetInteractor.createSnippet(requestWithNullCode));

                var requestWithNullDateModified = new CreateSnippet.RequestModel(SnippetDTO
                                .builder().title("").language("java").framework(null).code(null)
                                .description("rigor").resource(null).owner("noah").hidden(false)
                                .whenCreated(LocalDateTime.of(2020, 12, 5, 0, 0, 0))
                                .whenLastModified(null).build());

                assertThrows(NullPointerException.class,
                                () -> snippetInteractor.createSnippet(requestWithNullDateModified));
        }

        @Test
        void updateSnippet() {
                SnippetDTO moddedSnippet = SnippetDTO.builder().id(1l).title("test").language("js")
                                .code("expectToBe").description("rigor").owner("noah")
                                .whenCreated(LocalDateTime.of(2020, 12, 5, 0, 0, 0))
                                .whenLastModified(LocalDateTime.of(2020, 12, 5, 0, 0, 0)).build();

                var requestWithModifiedSnippet = new UpdateSnippet.RequestModel(moddedSnippet);
                var expected = new UpdateSnippet.ResponseModel(
                                "Snippet has been successfully updated.");
                var actual = snippetInteractor.updateSnippet(requestWithModifiedSnippet);

                assertEquals(expected, actual);

                var requestSameSnippet = new RetrievePublicSnippet.RequestModel(1);

                var previous = new RetrievePublicSnippet.ResponseModel(ownerSnippetMap.get("noah"));

                var actualSnippet = snippetInteractor.retrievePublicSnippet(requestSameSnippet);
                assertNotEquals(previous, actualSnippet);

                var shouldBe = new RetrievePublicSnippet.ResponseModel(moddedSnippet);
                assertEquals(shouldBe, actualSnippet);
        }

        @Test
        void updateSnippetOfOtherUserThrowsException() {
                var requestFromJamesNotNoah = new UpdateSnippet.RequestModel(SnippetDTO.builder()
                                .id(1l).title("test").language("js").code("expectToBe")
                                .description("rigor").owner("james")
                                .whenCreated(LocalDateTime.of(2020, 12, 5, 0, 0, 0))
                                .whenLastModified(LocalDateTime.of(2020, 12, 5, 0, 0, 0)).build());

                assertThrows(DifferentSnippetOwnerException.class,
                                () -> snippetInteractor.updateSnippet(requestFromJamesNotNoah));
        }

        @ParameterizedTest
        @ValueSource(longs = {1})
        void getASnippet(long id) {
                var request = new RetrievePublicSnippet.RequestModel(id);
                var expected = new RetrievePublicSnippet.ResponseModel(
                                ownerSnippetMap.get(snippetOwners[(int) id - 1]));
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
                var expected = new RetrieveSnippetOfUser.ResponseModel(ownerSnippetMap.get("kate"));
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
                for (pageSize = 5; pageSize > -1; pageSize--) {
                        var request = new RetrieveAllPublicSnippets.RequestModel(page, pageSize);
                        RetrieveAllPublicSnippets.ResponseModel response =
                                        snippetInteractor.RetrieveAllPublicSnippets(request);
                        assertEquals(5, response.numberOfSnippets());
                        assertIterableEquals(
                                        ownerSnippetMap.values().stream()
                                                        .sorted((x, y) -> Long.compare(x.id(),
                                                                        y.id()))
                                                        .limit(pageSize)
                                                        .collect(Collectors.toList()),
                                        response.snippets());
                }

                page = 2;
                var request = new RetrieveAllPublicSnippets.RequestModel(page, pageSize);
                var response = snippetInteractor.RetrieveAllPublicSnippets(request);
                assertIterableEquals(List.of(), response.snippets());
        }

        @Test
        void getMostRecentSnippets() {
                var request = new RetrieveRecentSnippets.RequestModel(3);
                RetrieveRecentSnippets.ResponseModel response =
                                snippetInteractor.RetrieveRecentSnippets(request);
                var recentSnippets = response.snippets();

                assertEquals(3, recentSnippets.size());
                assertIterableEquals(
                                ownerSnippetMap.values().stream()
                                                .sorted((x, y) -> y.whenLastModified()
                                                                .compareTo(x.whenLastModified()))
                                                .limit(3).collect(Collectors.toList()),
                                recentSnippets);
        }

}
