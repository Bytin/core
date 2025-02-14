package core.boundary;

import core.usecase.snippet.CreateSnippet;
import core.usecase.snippet.DeleteSnippetOfUser;
import core.usecase.snippet.SearchSnippets;
import core.usecase.snippet.RetrievePublicSnippet.*;

public interface SnippetIOBoundary extends IOBoundary {

    CreateSnippet.ResponseModel createSnippet(CreateSnippet.RequestModel request);

    ResponseModel retrievePublicSnippet(RequestModel request);

    core.usecase.snippet.UpdateSnippet.ResponseModel updateSnippet(
            core.usecase.snippet.UpdateSnippet.RequestModel request);

    Object retrieveSnippetOfUser(core.usecase.snippet.RetrieveSnippetOfUser.RequestModel request);

    core.usecase.snippet.RetrieveAllPublicSnippets.ResponseModel retrieveAllPublicSnippets(
            core.usecase.snippet.RetrieveAllPublicSnippets.RequestModel request);

    core.usecase.snippet.RetrieveRecentSnippets.ResponseModel retrieveRecentSnippets(
            core.usecase.snippet.RetrieveRecentSnippets.RequestModel request);

    core.usecase.snippet.RetrieveAllSnippetsOfUser.ResponseModel retrieveSnippetsOfUser(
            core.usecase.snippet.RetrieveAllSnippetsOfUser.RequestModel request);

    DeleteSnippetOfUser.ResponseModel deleteOne(
            core.usecase.snippet.DeleteSnippetOfUser.RequestModel request);

    SearchSnippets.ResponseModel searchPublicSnippets(SearchSnippets.RequestModel request);

    SearchSnippets.ResponseModel searchSnippetsOfUser(SearchSnippets.RequestModel request, String username);

}
