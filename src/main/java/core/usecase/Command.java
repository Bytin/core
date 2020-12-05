package core.usecase;

public interface Command<Request, Response> {

    Response execute(Request request);

}
