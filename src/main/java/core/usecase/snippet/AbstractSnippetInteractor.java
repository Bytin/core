package core.usecase.snippet;

import core.gateway.SnippetGateway;

public abstract class AbstractSnippetInteractor {
    
    SnippetGateway gateway;

    AbstractSnippetInteractor(SnippetGateway gateway){
        this.gateway = gateway;
    }

	public static class HiddenSnippetException extends IllegalStateException{
		private static final long serialVersionUID = 1L;
	}
    
	public static class NoSuchSnippetException extends IllegalStateException{
		private static final long serialVersionUID = 1L;
	}
}
