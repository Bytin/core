package core;

import java.time.LocalDateTime;
import java.util.*;
import core.boundary.SnippetIOBoundary;
import core.dto.SnippetDTO;
import core.usecase.snippet.CreateSnippet;

public class DummnyOwnedSnippets {

        final Map<String, SnippetDTO> ownerSnippetMap;
        String[] users;

        SnippetIOBoundary snippetInteractor;

        DummnyOwnedSnippets(SnippetIOBoundary snippetInteractor, String... users) {
                ownerSnippetMap = new HashMap<>();
                this.users = users;
                this.snippetInteractor = snippetInteractor;
        }

        public static SnippetDTO dummnySnippet(int id, String owner, boolean hidden) {
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
                        ownerSnippetMap.put(users[i], snippetDTO);
                }

        }

        SnippetDTO getSnippetOfUser(String user) {
                return ownerSnippetMap.get(user);
        }



        List<SnippetDTO> createManyDummySnippetsOwnedBy(String owner, boolean hidden,
                        int amount) {
                List<SnippetDTO> list = new ArrayList<>(amount);

                list.add(ownerSnippetMap.get(owner));

                int start = users.length;
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
