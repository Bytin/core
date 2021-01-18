package core.usecase.snippet;

import java.util.stream.Stream;
import core.entity.Snippet;
import core.gateway.SnippetGateway;
import core.usecase.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

public class SearchPublicSnippets extends AbstractSnippetInteractor implements Command<SearchPublicSnippets.RequestModel, SearchPublicSnippets.ResponseModel> {

    SearchPublicSnippets(SnippetGateway gateway) {
        super(gateway);
    }

    @Override
    public ResponseModel execute(RequestModel request) {
        Stream<Snippet> stream = gateway.streamAll();
        stream.close();
        return new ResponseModel();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestModel {
        String phrase, mode;
    }

    @Value
    public static class ResponseModel {

    }
    
}
