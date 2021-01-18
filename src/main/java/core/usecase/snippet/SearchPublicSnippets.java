package core.usecase.snippet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import core.dto.SnippetDTO;
import core.entity.Snippet;
import core.gateway.SnippetGateway;
import core.usecase.Command;
import core.utils.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

public class SearchPublicSnippets extends AbstractSnippetInteractor
        implements Command<SearchPublicSnippets.RequestModel, SearchPublicSnippets.ResponseModel> {

    SearchPublicSnippets(SnippetGateway gateway) {
        super(gateway);
    }

    @Override
    public ResponseModel execute(RequestModel request) {
        validate(request);
        
        var content = new ArrayList<SnippetDTO>();
        gateway.withPublicSnippetsStream(stream -> {
            List<SnippetDTO> snippets = List.of();
            switch (request.mode) {
                case SIMPLE -> snippets = simpleSearch(stream, request);
                case REGEX -> snippets = regexSearch(stream, request);
            }
            content.addAll(snippets);
        });

        return new ResponseModel(
                new Page<>(request.page, gateway.count() / request.size, request.size, content));
    }

    private List<SnippetDTO> simpleSearch(Stream<Snippet> stream, RequestModel req) {
        return stream.filter(snippet -> snippet.contains(req.phrase))
                .skip(req.page * req.size).limit(req.size).map(Snippet::toSnippetDto)
                .collect(Collectors.toList());
    }

    private List<SnippetDTO> regexSearch(Stream<Snippet> stream, RequestModel req) {
        return stream.filter(snippet -> snippet.containsPattern(req.phrase))
                .skip(req.page * req.size).limit(req.size).map(Snippet::toSnippetDto)
                .collect(Collectors.toList());
    }

    void validate(RequestModel req){
        if(req.page < 0) req.page = 0;
        if(req.size < 1) req.size = 1;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestModel {
        String phrase;
        Mode mode;
        int page, size;
    }

    @Value
    public static class ResponseModel {
        Page<SnippetDTO> page;
    }

    public enum Mode {
        SIMPLE, REGEX;
    }
}
