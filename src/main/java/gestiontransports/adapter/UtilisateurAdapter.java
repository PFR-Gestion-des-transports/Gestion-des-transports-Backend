package gestiontransports.adapter;

import gestiontransports.dto.utilisateur.UtilisateurDTO;
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
}
