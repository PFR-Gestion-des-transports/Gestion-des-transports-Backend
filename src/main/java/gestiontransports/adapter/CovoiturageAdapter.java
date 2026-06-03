package gestiontransports.adapter;

import gestiontransports.model.Covoiturage;
import gestiontransports.dto.covoiturage.CovoiturageDTO;
import gestiontransports.dto.covoiturage.CreerCovoiturageRequest;

/**
 * Adaptateur de conversion entre l'entité {@link Covoiturage} et ses DTOs.
 * Gère à la fois la sérialisation pour les réponses API et la désérialisation
 * des requêtes de création.
 */
public class CovoiturageAdapter {

    /**
     * Convertit une entité {@link Covoiturage} en {@link CovoiturageDTO} pour l'exposition via l'API.
     * Mappe les champs : id, nbrPlaceInitial, dateHeureDebut, statut, vehiculeId,
     * adresseDepart et adresseArrivee (via {@link AdresseAdapter#toDTO}).
     *
     * @param covoiturage l'entité covoiturage à convertir
     * @return le DTO de sortie correspondant
     */
    public static CovoiturageDTO toDTO(Covoiturage covoiturage) {
        CovoiturageDTO dto = new CovoiturageDTO();
        dto.setId(covoiturage.getId());
        dto.setNbrPlaceInitial(covoiturage.getNbrPlaceInitial());
        dto.setDateHeureDebut(covoiturage.getDateHeureDebut());
        dto.setStatut(covoiturage.getStatut());
        dto.setVehiculeId(covoiturage.getVehicule().getId());
        dto.setAdresseDepart(AdresseAdapter.toDTO(covoiturage.getAdresseDepart()));
        dto.setAdresseArrivee(AdresseAdapter.toDTO(covoiturage.getAdresseArrivee()));
        return dto;
    }

    /**
     * Convertit une {@link CreerCovoiturageRequest} en une nouvelle entité {@link Covoiturage}.
     * Initialise nbrPlaceRestante à la valeur de nbrPlaceInitial. Mappe également dateHeureDebut,
     * adresseDepart et adresseArrivee via {@link AdresseAdapter#toModel}.
     * Le conducteur, le véhicule et le statut doivent être renseignés séparément par le service.
     *
     * @param request la requête de création contenant les données du trajet
     * @return une nouvelle entité {@link Covoiturage} partiellement initialisée
     */
    public static Covoiturage toModel(CreerCovoiturageRequest request) {
        Covoiturage covoiturage = new Covoiturage();
        covoiturage.setNbrPlaceInitial(request.getNbrPlaceInitial());
        covoiturage.setNbrPlaceRestante(request.getNbrPlaceInitial());
        covoiturage.setDateHeureDebut(request.getDateHeureDebut());
        covoiturage.setAdresseDepart(AdresseAdapter.toModel(request.getAdresseDepart()));
        covoiturage.setAdresseArrivee(AdresseAdapter.toModel(request.getAdresseArrivee()));
        return covoiturage;
    }


}
