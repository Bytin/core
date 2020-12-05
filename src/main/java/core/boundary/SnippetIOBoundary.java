package core.boundary;

import core.usecase.snippet.CreateSnippet;
import core.usecase.snippet.RetrievePublicSnippet.*;

public interface SnippetIOBoundary extends IOBoundary{

	CreateSnippet.ResponseModel createSnippet(CreateSnippet.RequestModel request);

	ResponseModel retrieveSnippet(RequestModel request);



}
