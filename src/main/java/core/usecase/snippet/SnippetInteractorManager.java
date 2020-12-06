package core.usecase.snippet;

import core.boundary.SnippetIOBoundary;
import core.gateway.SnippetGateway;
import core.gateway.UserGateway;
import core.usecase.snippet.CreateSnippet.RequestModel;
import core.usecase.snippet.RetrievePublicSnippet.ResponseModel;

public class SnippetInteractorManager extends AbstractSnippetInteractor implements SnippetIOBoundary {

    UserGateway userGateway;

    public SnippetInteractorManager(SnippetGateway gateway, UserGateway userGateway) {
        super(gateway);
        this.userGateway = userGateway;
    }

    @Override
    public CreateSnippet.ResponseModel createSnippet(RequestModel request) {
        return new CreateSnippet(gateway, userGateway).execute(request);
    }

    @Override
    public ResponseModel retrievePublicSnippet(core.usecase.snippet.RetrievePublicSnippet.RequestModel request) {
        return new RetrievePublicSnippet(gateway).execute(request);
    }

    @Override
    public core.usecase.snippet.UpdateSnippet.ResponseModel updateSnippet(
            core.usecase.snippet.UpdateSnippet.RequestModel request) {
        return new UpdateSnippet(gateway, userGateway).execute(request);
    }

    @Override
    public RetrieveSnippetOfUser.ResponseModel retrieveSnippetOfUser(core.usecase.snippet.RetrieveSnippetOfUser.RequestModel request) {
        return new RetrieveSnippetOfUser(gateway).execute(request);
    }

    @Override
    public core.usecase.snippet.RetrieveAllPublicSnippets.ResponseModel RetrieveAllPublicSnippets(
                    core.usecase.snippet.RetrieveAllPublicSnippets.RequestModel request) {
            return new RetrieveAllPublicSnippets(gateway).execute(request);
    }

    @Override
    public core.usecase.snippet.RetrieveRecentSnippets.ResponseModel RetrieveRecentSnippets(
                    core.usecase.snippet.RetrieveRecentSnippets.RequestModel request) {
            return new RetrieveRecentSnippets(gateway).execute(request);
    }

}
