package core;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;
import core.boundary.SnippetIOBoundary;
import core.dto.SnippetDTO;
import core.dto.UserDTO;
import core.usecase.snippet.CreateSnippet;

public class DummnyOwnedSnippets {

        private final Map<String, SnippetDTO> ownerSnippetMap;
        private UserDTO[] users;

        private SnippetIOBoundary snippetInteractor;

        DummnyOwnedSnippets(SnippetIOBoundary snippetInteractor, String... users) {
                ownerSnippetMap = new HashMap<>();
                this.users = userDtosFromUsernames(users);
                this.snippetInteractor = snippetInteractor;
        }

        public static SnippetDTO dummnySnippet(int id, UserDTO owner, boolean hidden) {
                return SnippetDTO.builder().id(id).title("test").language("java").framework(null)
                                .code("assert").description("rigor").resource(null).owner(owner)
                                .hidden(hidden).whenCreated(LocalDateTime.of(2020, 12, 5, id, 0, 0))
                                .whenLastModified(LocalDateTime.of(2020, 12, 5, id, 0, 0)).build();
        }

        void createSnippets(TestCallback<SnippetDTO> testCallback) {
                boolean[] hidden = new boolean[] {false, true, true, false, false};

                for (int i = 0; i < users.length; i++) {
                        SnippetDTO snippetDTO = dummnySnippet(i + 1, users[i], hidden[i]);
                        testCallback.runTest(snippetDTO);
                        ownerSnippetMap.put(users[i].username(), snippetDTO);
                }

        }

        SnippetDTO getSnippetOfUser(String user) {
                return ownerSnippetMap.get(user);
        }

        Map<String, SnippetDTO> getMap() {
                return ownerSnippetMap;
        }

        UserDTO getLastUser() {
                return users[users.length - 1];
        }

        UserDTO getUserDTO(long id) {
                return users[(int) id - 1];
        }

        UserDTO getUserDTO(String user) {
                return Stream.of(users).filter(usr -> usr.username().equals(user)).findFirst()
                                .orElse(new UserDTO(0, "user", "a@g.com"));
        }

        List<SnippetDTO> createManyDummySnippetsOwnedBy(UserDTO owner, boolean hidden, int amount) {
                List<SnippetDTO> list = new ArrayList<>(amount);

                list.add(ownerSnippetMap.get(owner.username()));

                int start = users.length;
                for (int i = start; i < start + amount; i++) {
                        SnippetDTO snippetDTO = dummnySnippet(i + 1, owner, hidden);
                        list.add(snippetDTO);
                        snippetInteractor.createSnippet(new CreateSnippet.RequestModel(snippetDTO));
                }
                return list;
        }

        UserDTO[] userDtosFromUsernames(String[] users) {
                var dtos = new UserDTO[users.length];
                for (int i = 0; i < users.length; i++) {
                        dtos[i] = new UserDTO(i + 1, users[i], "we@g.com");
                }
                return dtos;
        }

        UserDTO[] getUsers() {
                return users;
        }

        @FunctionalInterface
        public interface TestCallback<T> {
                void runTest(T param);
        }



}
