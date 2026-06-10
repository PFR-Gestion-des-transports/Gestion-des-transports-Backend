package gestiontransports.dto.securite;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO d'entrée contenant les données d'inscription d'un nouvel utilisateur,
 * reçu via POST /auth/inscription.
 */
public class CreerCompteRequestDTO {

    @NotBlank
    private String prenom;

    @NotBlank
    private String nom;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String motDePasse;

    @NotBlank
    private String ville;

    @NotBlank
    private String rue;

    @NotBlank
    private String numeroRue;

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getRue() { return rue; }
    public void setRue(String rue) { this.rue = rue; }

    public String getNumeroRue() { return numeroRue; }
    public void setNumeroRue(String numeroRue) { this.numeroRue = numeroRue; }
}
