package gestiontransports.exception;

import gestiontransports.dto.erreur.ErreurDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErreurDTO> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ErreurDTO(ex.getStatusCode().value(), ex.getReason()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErreurDTO> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .badRequest()
                .body(new ErreurDTO(400, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErreurDTO> handleException(Exception ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Une erreur interne est survenue";
        return ResponseEntity
                .internalServerError()
                .body(new ErreurDTO(500, message));
    }
}
