package gestiontransports.adapter;

import gestiontransports.dto.adresse.AdresseInputDTO;
import gestiontransports.dto.adresse.AdresseOutputDTO;
import gestiontransports.model.Adresse;
import org.springframework.lang.NonNull;

/**
 * Adaptateur de conversion entre l'entité {@link Adresse} et ses DTOs.
 * Centralise la logique de mapping pour éviter toute dépendance directe aux entités JPA
 * dans les couches contrôleur et service.
 */
public class AdresseAdapter {

    /**
     * Convertit une entité {@link Adresse} en {@link AdresseOutputDTO} pour l'exposition via l'API.
     * Mappe les champs : id, ville, rue, numeroRue.
     *
     * @param adresse l'entité adresse à convertir
     * @return le DTO de sortie correspondant
     */
    public static AdresseOutputDTO toDTO(Adresse adresse) {
        AdresseOutputDTO dto = new AdresseOutputDTO();
        dto.setId(adresse.getId());
        dto.setVille(adresse.getVille());
        dto.setRue(adresse.getRue());
        dto.setNumeroRue(adresse.getNumeroRue());
        return dto;
    }

    /**
     * Convertit un {@link AdresseInputDTO} reçu en entrée d'API en une nouvelle entité {@link Adresse}.
     * Mappe les champs : ville, rue, numeroRue. L'identifiant n'est pas renseigné (généré par JPA).
     *
     * @param dto le DTO d'entrée contenant les données de l'adresse
     * @return une nouvelle entité {@link Adresse} prête à être persistée
     */
    public static @NonNull Adresse toModel(AdresseInputDTO dto) {
        Adresse adresse = new Adresse();
        adresse.setVille(dto.getVille());
        adresse.setRue(dto.getRue());
        adresse.setNumeroRue(dto.getNumeroRue());
        return adresse;
    }
}
