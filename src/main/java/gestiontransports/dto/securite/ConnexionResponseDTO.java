package gestiontransports.dto.securite;

public class ConnexionResponseDTO {

    private String token;

    public ConnexionResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() { return token; }
}
