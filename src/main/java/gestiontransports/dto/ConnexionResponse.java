package gestiontransports.dto;

public class ConnexionResponse {

    private String token;

    public ConnexionResponse(String token) {
        this.token = token;
    }

    public String getToken() { return token; }
}
