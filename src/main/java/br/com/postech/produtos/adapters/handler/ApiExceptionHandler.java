package br.com.postech.produtos.adapters.handler;

import br.com.postech.produtos.business.exceptions.BadRequestException;
import br.com.postech.produtos.business.exceptions.NegocioException;
import br.com.postech.produtos.business.exceptions.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler {


    @ExceptionHandler(NegocioException.class)
    public final ResponseEntity<ExceptionResponse> handleTo(NegocioException e) {
        return new ResponseEntity<>(new ExceptionResponse(ErrorType.PROCESS_FAILURE,
                e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ExceptionResponse> handleTo(BadRequestException e) {
        return new ResponseEntity<>(new ExceptionResponse(ErrorType.VALIDATION_FAILURE,
                e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleTo(NotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(ErrorType.RESOURCE_NOT_FOUND,
                e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<ExceptionResponse> handleTo(DataIntegrityViolationException e) {
        return new ResponseEntity<>(new ExceptionResponse(ErrorType.PROCESS_FAILURE,
                e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleGenericException(Exception e) {
        return new ResponseEntity<>(new ExceptionResponse(ErrorType.GENERIC_SERVER_ERROR,
                "Erro inesperado encontrado no servidor durante o processamento da solicitação"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
        return new ResponseEntity<>(new ExceptionResponse(ErrorType.VALIDATION_FAILURE, errors), HttpStatus.BAD_REQUEST);
    }

    protected static class ExceptionResponse {
        private final ErrorType errorType;
        private String errorMessage;

        public ExceptionResponse(ErrorType errorType) {
            this.errorType = errorType;
        }

        public ExceptionResponse(ErrorType errorType, String errorMessage) {
            this.errorType = errorType;
            this.errorMessage = errorMessage;
        }

        public ErrorType getErrorType() {
            return errorType;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    protected enum ErrorType {
        RESOURCE_NOT_FOUND, PROCESS_FAILURE, VALIDATION_FAILURE, GENERIC_SERVER_ERROR
    }
}
