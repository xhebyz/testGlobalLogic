package cl.saraos.bank.handler;

import cl.saraos.bank.domain.ErrorResponse;
import cl.saraos.bank.exceptions.UnauthorizedException;
import cl.saraos.bank.exceptions.UserExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GenericErrorHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        ErrorResponse errorResponse = ErrorResponse.builder().error(
                Arrays.asList(ErrorResponse.Error.builder()
                        .codigo(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .detail(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build())
        ).build();

        return ResponseEntity.status(errorResponse.getError().stream().findFirst().get().getCodigo()).body(errorResponse);
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<ErrorResponse> handleUserExistException(UserExistException ex) {

        ErrorResponse errorResponse = ErrorResponse.builder().error(
                Arrays.asList(ErrorResponse.Error.builder()
                                .codigo(HttpStatus.FORBIDDEN.value())
                                .detail(ex.getMessage())
                                .timestamp(LocalDateTime.now())
                        .build())
        ).build();

        return ResponseEntity.status(errorResponse.getError().stream().findFirst().get().getCodigo()).body(errorResponse);
    }


    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {

        ErrorResponse errorResponse = ErrorResponse.builder().error(
                Arrays.asList(ErrorResponse.Error.builder()
                        .codigo(HttpStatus.UNAUTHORIZED.value())
                        .detail(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build())
        ).build();


        return ResponseEntity.status(errorResponse.getError().stream().findFirst().get().getCodigo()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();

        ErrorResponse errorResponse = ErrorResponse.builder().error(
                result.getFieldErrors().stream().map(
            fieldError ->
                    ErrorResponse.Error.builder()
                            .codigo(HttpStatus.BAD_REQUEST.value())
                            .detail(fieldError.getDefaultMessage())
                            .timestamp(LocalDateTime.now())
                            .build()
        ).collect(Collectors.toList())).build();


        return ResponseEntity.status(errorResponse.getError().stream().findFirst().get().getCodigo()).body(errorResponse);
    }
}
