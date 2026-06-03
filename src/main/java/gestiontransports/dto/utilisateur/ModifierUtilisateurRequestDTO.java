package gestiontransports.dto.utilisateur;

import gestiontransports.dto.adresse.AdresseInputDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ModifierUtilisateurRequestDTO {

    @NotBlank
    private String prenom;

    @NotBlank
    private String nom;

    @NotNull
    @Valid
    private AdresseInputDTO adresse;

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public AdresseInputDTO getAdresse() { return adresse; }
    public void setAdresse(AdresseInputDTO adresse) { this.adresse = adresse; }
}
