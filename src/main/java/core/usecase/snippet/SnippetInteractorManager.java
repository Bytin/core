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
    public ResponseModel retrieveSnippet(core.usecase.snippet.RetrievePublicSnippet.RequestModel request) {
        return new RetrievePublicSnippet(gateway).execute(request);
    }

}
