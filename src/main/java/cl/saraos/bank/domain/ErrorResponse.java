package cl.saraos.bank.domain;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {

    private List<Error> error;

    @Data
    class Error{
        private long timestamp;
        private int codigo;
        private String detail;
    }

}
