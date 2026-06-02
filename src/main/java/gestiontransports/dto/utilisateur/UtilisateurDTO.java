package gestiontransports.dto.utilisateur;

import gestiontransports.dto.adresse.AdresseDTO;
import gestiontransports.model.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class UtilisateurDTO {

    
    @NotBlank
    private String prenom;

    @NotBlank
    private String nom;

    @NotBlank
    @Email
    private String email;

    @NotNull
    @Valid
    private AdresseDTO adresse;

    @NotEmpty
    private Set<Role> roles;

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public AdresseDTO getAdresse() { return adresse; }
    public void setAdresse(AdresseDTO adresse) { this.adresse = adresse; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}
