package gestiontransports.dto.securite;

/**
 * DTO de sortie contenant le token JWT retourné après une connexion ou une inscription réussie.
 */
public class ConnexionResponseDTO {

    /** Token JWT à fournir dans l'en-tête Authorization des requêtes sécurisées. */
    private String token;

    /**
     * Construit la réponse d'authentification avec le token JWT généré.
     *
     * @param token le token JWT à transmettre au client
     */
    public ConnexionResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() { return token; }
}
