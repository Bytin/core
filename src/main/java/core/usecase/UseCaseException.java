package core.usecase;

public class UseCaseException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public UseCaseException(String msg) {
                super(msg);
        }


        public static class UserAlreadyExistsException extends UseCaseException {
                private static final long serialVersionUID = 1L;

                public UserAlreadyExistsException(String username) {
                        super(username + " already exists.");
                }
        }

        public static class NoSuchUserException extends UseCaseException {
                private static final long serialVersionUID = 1L;

                public NoSuchUserException(String username) {
                        super(username + " already exits.");
                }
        }

        public static class HiddenSnippetException extends UseCaseException {
                public HiddenSnippetException(long id) {
                        super("Snippet #" + id + " is a private snippet.");
                }

                private static final long serialVersionUID = 1L;
        }

        public static class NoSuchSnippetException extends UseCaseException {
                public NoSuchSnippetException(long id) {
                        super("No snippet exists by id #" + id);
                }

                private static final long serialVersionUID = 1L;
        }

        public static class DifferentSnippetOwnerException extends UseCaseException {
                public DifferentSnippetOwnerException(String username) {
                        super(username + " doesn't own that snippet.");
                }

                private static final long serialVersionUID = 1L;
        }



}
