package core.usecase.snippet;

import core.gateway.SnippetGateway;

public abstract class AbstractSnippetInteractor {
    
    SnippetGateway gateway;

    AbstractSnippetInteractor(SnippetGateway gateway){
        this.gateway = gateway;
    }

	//TODO should I just use Illegal state exceptions with custem messages?
	public static class HiddenSnippetException extends IllegalStateException{
		private static final long serialVersionUID = 1L;
	}
    
	public static class NoSuchSnippetException extends IllegalStateException{
		private static final long serialVersionUID = 1L;
	}

	public static class DifferentSnippetOwnerException extends IllegalStateException{
		public DifferentSnippetOwnerException(String string) {
			super(string);
		}
		private static final long serialVersionUID = 1L;
	}
}
