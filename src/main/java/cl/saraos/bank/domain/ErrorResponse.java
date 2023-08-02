package cl.saraos.bank.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ErrorResponse {

    private List<Error> error;

    @Data
    @Builder
    public static class Error{
        private LocalDateTime timestamp;
        private int codigo;
        private String detail;
    }

}
