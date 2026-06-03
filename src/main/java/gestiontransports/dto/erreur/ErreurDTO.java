package gestiontransports.dto.erreur;

/**
 * DTO de sortie représentant le corps de réponse standard pour les erreurs HTTP,
 * retourné par le gestionnaire global d'exceptions.
 */
public class ErreurDTO {

    /** Code de statut HTTP de l'erreur (ex. 400, 404, 403). */
    private int status;
    /** Message explicatif de l'erreur à destination du client. */
    private String message;

    public ErreurDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
}
