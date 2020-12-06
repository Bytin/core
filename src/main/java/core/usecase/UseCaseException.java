package core.usecase;

public class UseCaseException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public UseCaseException(String msg) {
                super(msg);
        }


        public static class UserAlreadyExistsException extends UseCaseException {
                private static final long serialVersionUID = 1L;

                public UserAlreadyExistsException(String msg) {
                        super(msg);
                }
        }

        public static class NoSuchUserException extends UseCaseException {
                private static final long serialVersionUID = 1L;

                public NoSuchUserException(String msg) {
                        super(msg);
                }
        }

        public static class HiddenSnippetException extends UseCaseException {
                public HiddenSnippetException(String msg) {
                        super(msg);
                }

                private static final long serialVersionUID = 1L;
        }

        public static class NoSuchSnippetException extends UseCaseException {
                public NoSuchSnippetException(String msg) {
                        super(msg);
                }

                private static final long serialVersionUID = 1L;
        }

        public static class DifferentSnippetOwnerException extends UseCaseException {
                public DifferentSnippetOwnerException(String string) {
                        super(string);
                }

                private static final long serialVersionUID = 1L;
        }



}
