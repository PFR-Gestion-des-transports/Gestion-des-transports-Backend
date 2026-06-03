package gestiontransports.dto.utilisateur;

import gestiontransports.dto.adresse.AdresseOutputDTO;
import gestiontransports.enums.Role;
import java.util.Set;

public class UtilisateurDTO {

    private int id;
    private String prenom;
    private String nom;
    private String email;
    private AdresseOutputDTO adresse;
    private Set<Role> roles;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public AdresseOutputDTO getAdresse() { return adresse; }
    public void setAdresse(AdresseOutputDTO adresse) { this.adresse = adresse; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}
