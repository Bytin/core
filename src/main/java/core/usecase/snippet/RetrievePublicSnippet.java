package core.usecase.snippet;

import core.dto.SnippetDTO;
import core.entity.Snippet;
import core.gateway.SnippetGateway;
import core.usecase.Command;
import core.usecase.UseCaseException.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;

public class RetrievePublicSnippet extends AbstractSnippetInteractor implements
                Command<RetrievePublicSnippet.RequestModel, RetrievePublicSnippet.ResponseModel> {

        RetrievePublicSnippet(SnippetGateway gateway) {
                super(gateway);
        }

        @Override
        public ResponseModel execute(RequestModel req) {
                Snippet snippet = gateway.findById(req.id)
                                .orElseThrow(() -> new NoSuchSnippetException(req.id));

                if (snippet.isHidden())
                        throw new HiddenSnippetException(snippet.getId());

                return new ResponseModel(snippet.toSnippetDto());
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RequestModel {
                long id;
        }

        @Value
        public static class ResponseModel {
                @NonNull
                SnippetDTO snippet;
        }

}
