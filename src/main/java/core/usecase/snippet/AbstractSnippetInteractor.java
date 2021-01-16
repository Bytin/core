package core.usecase.snippet;

import core.gateway.SnippetGateway;

public abstract class AbstractSnippetInteractor {

    SnippetGateway gateway;

    AbstractSnippetInteractor(SnippetGateway gateway) {
        this.gateway = gateway;
    }

}
