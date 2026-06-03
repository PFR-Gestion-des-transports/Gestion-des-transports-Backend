package gestiontransports.adapter;

import gestiontransports.dto.utilisateur.UtilisateurDTO;
import gestiontransports.model.Utilisateur;

/**
 * Adaptateur de conversion entre l'entité {@link Utilisateur} et son DTO de sortie.
 * Garantit que les données sensibles (mot de passe) ne sont jamais exposées via l'API.
 */
public class UtilisateurAdapter {

    /**
     * Convertit une entité {@link Utilisateur} en {@link UtilisateurDTO} pour l'exposition via l'API.
     * Mappe les champs : id, prenom, nom, email, adresse (via {@link AdresseAdapter#toDTO}) et roles.
     * Le mot de passe n'est jamais inclus dans le DTO.
     *
     * @param utilisateur l'entité utilisateur à convertir
     * @return le DTO de sortie correspondant
     */
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
