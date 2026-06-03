package gestiontransports.adapter;

import gestiontransports.model.ReservationCovoiturage;
import gestiontransports.dto.reservation.ReservationCovoiturageDTO;

public class ReservationCovoiturageAdapter {
    
    public static ReservationCovoiturageDTO toDTO(ReservationCovoiturage reservation) {
        ReservationCovoiturageDTO dto = new ReservationCovoiturageDTO();
        dto.setId(reservation.getId());
        dto.setCovoiturage(CovoiturageAdapter.toDTO(reservation.getCovoiturage()));
        dto.setUtilisateur(UtilisateurAdapter.toDTO(reservation.getUtilisateur()));
        return dto;
    }
}
