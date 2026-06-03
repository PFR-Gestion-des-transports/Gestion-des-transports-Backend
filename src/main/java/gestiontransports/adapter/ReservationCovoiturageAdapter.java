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

    public static ReservationCovoiturage toModel(ReservationCovoiturageDTO dto) {
        ReservationCovoiturage reservation = new ReservationCovoiturage();
        reservation.setId(dto.getId());
        reservation.setCovoiturage(CovoiturageAdapter.toModel(dto.getCovoiturage()));
        reservation.setUtilisateur(UtilisateurAdapter.toModel(dto.getUtilisateur()));
        return reservation;
    }
}
