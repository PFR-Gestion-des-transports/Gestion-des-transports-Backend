package gestiontransports.adapter;

import gestiontransports.dto.vehicule.VehiculeDTO;
import gestiontransports.model.Vehicule;

public class VehiculeAdapter {

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
        dto.setEstVehiculeService(vehicule.getEstVehiculeService());
        dto.setStatutVehicule(vehicule.getStatutVehicule());
        dto.setUtilisateurId(vehicule.getUtilisateur().getId());
        return dto;
    }
}
