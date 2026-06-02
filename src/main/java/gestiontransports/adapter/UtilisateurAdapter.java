package gestiontransports.adapter;

import gestiontransports.dto.adresse.AdresseDTO;
import gestiontransports.dto.utilisateur.UtilisateurDTO;
import gestiontransports.model.Adresse;
import gestiontransports.model.Utilisateur;

public class UtilisateurAdapter {

    public static UtilisateurDTO toDTO(Utilisateur utilisateur) {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setId(utilisateur.getId());
        dto.setPrenom(utilisateur.getPrenom());
        dto.setNom(utilisateur.getNom());
        dto.setEmail(utilisateur.getEmail());
        dto.setAdresse(AdresseAdapter.toDTO(utilisateur.getAdresse()));
        dto.setRoles(utilisateur.getRoles());
        return dto;
    }

    public static Utilisateur toModel(UtilisateurDTO dto) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(dto.getId());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setNom(dto.getNom());
        utilisateur.setEmail(dto.getEmail());
        if (dto.getAdresse() != null) {
            Adresse adresse = AdresseAdapter.toModel(dto.getAdresse());
            utilisateur.setAdresse(adresse);
        }
        utilisateur.setRoles(dto.getRoles());
        return utilisateur;
    }
}
