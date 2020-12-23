package core;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import core.boundary.SnippetIOBoundary;
import core.dto.SnippetDTO;
import core.dto.UserDTO;
import core.entity.User;
import core.usecase.snippet.CreateSnippet;
import lombok.Getter;

public class DummnyOwnedSnippets {

        private @Getter final Map<String, SnippetDTO> ownerSnippetMap;
        private @Getter List<User> users;

        private SnippetIOBoundary snippetInteractor;

        DummnyOwnedSnippets(SnippetIOBoundary snippetInteractor, Collection<User> users) {
                ownerSnippetMap = new HashMap<>();
                this.users = users.stream().collect(Collectors.toList());
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

                for (int i = 0; i < users.size(); i++) {
                        SnippetDTO snippetDTO = dummnySnippet(i + 1, users.get(i).toUserDto(), hidden[i]);
                        testCallback.runTest(snippetDTO);
                        ownerSnippetMap.put(users.get(i).getUsername(), snippetDTO);
                }

        }

        SnippetDTO getSnippetOfUser(String user) {
                return ownerSnippetMap.get(user);
        }

        List<SnippetDTO> createManyDummySnippetsOwnedBy(UserDTO owner, boolean hidden, int amount) {
                List<SnippetDTO> list = new ArrayList<>(amount);

                list.add(ownerSnippetMap.get(owner.username()));

                int start = users.size();
                for (int i = start; i < start + amount; i++) {
                        SnippetDTO snippetDTO = dummnySnippet(i + 1, owner, hidden);
                        list.add(snippetDTO);
                        snippetInteractor.createSnippet(new CreateSnippet.RequestModel(snippetDTO));
                }
                return list;
        }

        @FunctionalInterface
        public interface TestCallback<T> {
                void runTest(T param);
        }



}
