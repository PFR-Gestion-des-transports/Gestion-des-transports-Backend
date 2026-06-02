package gestiontransports.dto.securite;

import gestiontransports.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

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

    @NotEmpty
    private Set<Role> roles;

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

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}
