package gestiontransports.adapter;

import gestiontransports.model.Covoiturage;
import gestiontransports.dto.covoiturage.CovoiturageDTO;
import gestiontransports.dto.covoiturage.CreerCovoiturageRequest;

public class CovoiturageAdapter {
    public static CovoiturageDTO toDTO(Covoiturage covoiturage) {
        CovoiturageDTO dto = new CovoiturageDTO();
        dto.setNbrPlaceInitial(covoiturage.getNbrPlaceInitial());
        dto.setDateHeureDebut(covoiturage.getDateHeureDebut());
        dto.setAdresseDepart(AdresseAdapter.toDTO(covoiturage.getAdresseDepart()));
        dto.setAdresseArrivee(AdresseAdapter.toDTO(covoiturage.getAdresseArrivee()));
        return dto;
    }

    public static Covoiturage toModel(CovoiturageDTO dto) {
        Covoiturage covoiturage = new Covoiturage();
        covoiturage.setNbrPlaceInitial(dto.getNbrPlaceInitial());
        covoiturage.setDateHeureDebut(dto.getDateHeureDebut());
        covoiturage.setAdresseDepart(AdresseAdapter.toModel(dto.getAdresseDepart()));
        covoiturage.setAdresseArrivee(AdresseAdapter.toModel(dto.getAdresseArrivee()));
        return covoiturage;
    }

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
