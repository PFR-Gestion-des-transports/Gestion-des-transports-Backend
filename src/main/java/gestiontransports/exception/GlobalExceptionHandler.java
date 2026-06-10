package gestiontransports.exception;

import gestiontransports.dto.erreur.ErreurDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

/**
 * Gestionnaire global des exceptions pour l'ensemble de l'API REST.
 * Intercepte les exceptions levées dans les contrôleurs et les traduit en réponses HTTP
 * structurées via {@link ErreurDTO}, avec le code HTTP et le message appropriés.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Traite les exceptions {@link ResponseStatusException} levées explicitement dans les services
     * ou contrôleurs (ex. : 404 Not Found, 403 Forbidden).
     *
     * @param ex l'exception contenant le code HTTP et le message de raison
     * @return une réponse HTTP avec le statut et le corps d'erreur correspondants
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErreurDTO> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ErreurDTO(ex.getStatusCode().value(), ex.getReason()));
    }

    /**
     * Traite les erreurs de validation des DTOs annotés avec les contraintes Bean Validation
     * (ex. : {@code @NotNull}, {@code @Size}).
     * Concatène tous les messages d'erreur de champ en une seule chaîne lisible.
     *
     * @param ex l'exception contenant la liste des violations de contraintes
     * @return une réponse HTTP 400 Bad Request avec le détail des champs invalides
     */
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

    /**
     * Filet de sécurité interceptant toute exception non gérée explicitement par les autres handlers.
     * Retourne une réponse HTTP 500 avec le message de l'exception ou un message générique si absent.
     *
     * @param ex l'exception inattendue
     * @return une réponse HTTP 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErreurDTO> handleException(Exception ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Une erreur interne est survenue";
        return ResponseEntity
                .internalServerError()
                .body(new ErreurDTO(500, message));
    }
}
