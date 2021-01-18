package core.usecase.snippet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

public class SearchSnippets extends AbstractSnippetInteractor
        implements Command<SearchSnippets.RequestModel, SearchSnippets.ResponseModel> {

    SearchSnippets(SnippetGateway gateway) {
        super(gateway);
    }

    @Override
    public ResponseModel execute(RequestModel request) {
        validate(request);

        var content = new ArrayList<SnippetDTO>();
        gateway.withSnippetsStream(stream -> {
            List<SnippetDTO> snippets = List.of();

            if (request.predicate != null)
                stream = stream.filter(request.predicate);

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
        return stream.filter(snippet -> snippet.contains(req.phrase)).skip(req.page * req.size)
                .limit(req.size).map(Snippet::toSnippetDto).collect(Collectors.toList());
    }

    private List<SnippetDTO> regexSearch(Stream<Snippet> stream, RequestModel req) {
        return stream.filter(snippet -> snippet.containsPattern(req.phrase))
                .skip(req.page * req.size).limit(req.size).map(Snippet::toSnippetDto)
                .collect(Collectors.toList());
    }

    void validate(RequestModel req) {
        if (req.page < 0)
            req.page = 0;
        if (req.size < 1)
            req.size = 1;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class RequestModel {
        @NonNull
        String phrase;
        @NonNull
        Mode mode;
        @NonNull
        Integer page, size;
        public Predicate<Snippet> predicate;
    }

    @Value
    public static class ResponseModel {
        Page<SnippetDTO> page;
    }

    public enum Mode {
        SIMPLE, REGEX;
    }
}
