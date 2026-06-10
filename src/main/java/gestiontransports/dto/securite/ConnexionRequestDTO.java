package gestiontransports.dto.securite;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO d'entrée contenant les identifiants de connexion (email et mot de passe),
 * reçu via POST /auth/connexion.
 */
public class ConnexionRequestDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String motDePasse;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
}
