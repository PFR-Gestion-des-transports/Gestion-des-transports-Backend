package gestiontransports.dto.erreur;

public class ErreurDTO {

    private int status;
    private String message;

    public ErreurDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
}
