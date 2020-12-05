package core.usecase;

public interface Command<Request, Response> {

    void validate(Request request) throws IllegalRequestModelException;

    Response execute(Request request);

    class IllegalRequestModelException extends IllegalArgumentException {
        private static final long serialVersionUID = 1L;

        public IllegalRequestModelException(String string) {
            super(string);
        }
    }

    default void validateRequestExceptWhenAnyNull(Runnable runnable) {
        try {
            runnable.run();
        } catch (NullPointerException e) {
            throw new IllegalRequestModelException("No nulls allowed in the request.");
        }
    }
}
