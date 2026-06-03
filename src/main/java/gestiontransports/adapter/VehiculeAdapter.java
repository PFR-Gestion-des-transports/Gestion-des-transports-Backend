package gestiontransports.adapter;

import gestiontransports.dto.vehicule.VehiculeDTO;
import gestiontransports.model.Vehicule;

/**
 * Adaptateur de conversion entre l'entité {@link Vehicule} et son DTO de sortie.
 * Expose uniquement les données nécessaires à l'API sans révéler les entités JPA.
 */
public class VehiculeAdapter {

    /**
     * Convertit une entité {@link Vehicule} en {@link VehiculeDTO} pour l'exposition via l'API.
     * Mappe les champs : id, immatriculation, marque, modele, urlPhoto, co2Km, nombreDePlace,
     * categorie, motorisation, estVehiculeService, statutVehicule et l'identifiant du propriétaire
     * (utilisateurId).
     *
     * @param vehicule l'entité véhicule à convertir
     * @return le DTO de sortie correspondant
     */
    public static VehiculeDTO toDTO(Vehicule vehicule) {
        VehiculeDTO dto = new VehiculeDTO();
        dto.setId(vehicule.getId());
        dto.setImmatriculation(vehicule.getImmatriculation());
        dto.setMarque(vehicule.getMarque());
        dto.setModele(vehicule.getModele());
        dto.setUrlPhoto(vehicule.getUrlPhoto());
        dto.setCo2Km(vehicule.getCo2Km());
        dto.setNombreDePlace(vehicule.getNombreDePlace());
        dto.setCategorie(vehicule.getCategorie());
        dto.setMotorisation(vehicule.getMotorisation());
        dto.setEstVehiculeService(vehicule.isEstVehiculeService());
        dto.setStatutVehicule(vehicule.getStatutVehicule());
        dto.setUtilisateurId(vehicule.getUtilisateur().getId());
        return dto;
    }
}
