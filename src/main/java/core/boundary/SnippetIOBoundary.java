package core.boundary;

import core.usecase.snippet.CreateSnippet;
import core.usecase.snippet.RetrievePublicSnippet.*;

public interface SnippetIOBoundary extends IOBoundary{

	CreateSnippet.ResponseModel createSnippet(CreateSnippet.RequestModel request);

	ResponseModel retrievePublicSnippet(RequestModel request);

	core.usecase.snippet.UpdateSnippet.ResponseModel updateSnippet(
			core.usecase.snippet.UpdateSnippet.RequestModel request);

	Object retrieveSnippetOfUser(core.usecase.snippet.RetrieveSnippetOfUser.RequestModel request);

  core.usecase.snippet.RetrieveAllPublicSnippets.ResponseModel RetrieveAllPublicSnippets(
      core.usecase.snippet.RetrieveAllPublicSnippets.RequestModel request);

  core.usecase.snippet.RetrieveRecentSnippets.ResponseModel RetrieveRecentSnippets(
      core.usecase.snippet.RetrieveRecentSnippets.RequestModel request);



}
