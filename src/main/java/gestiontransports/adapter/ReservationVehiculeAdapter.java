package gestiontransports.adapter;

import gestiontransports.dto.reservationvehicule.ReservationVehiculeDTO;
import gestiontransports.dto.reservationvehicule.ReserverVehiculeServiceDTO;
import gestiontransports.enums.StatutReservation;
import gestiontransports.model.ReservationVehicule;
import gestiontransports.model.Utilisateur;
import gestiontransports.model.Vehicule;
import org.springframework.lang.NonNull;

public class ReservationVehiculeAdapter {

    public static ReservationVehiculeDTO toDTO(ReservationVehicule reservation) {
        ReservationVehiculeDTO dto = new ReservationVehiculeDTO();
        dto.setId(reservation.getId());
        dto.setDateHeureDebut(reservation.getDateHeureDebut());
        dto.setDateHeureFin(reservation.getDateHeureFin());
        dto.setStatutReservation(reservation.getStatutReservation());
        dto.setUtilisateur(UtilisateurAdapter.toDTO(reservation.getUtilisateur()));
        dto.setVehicule(VehiculeAdapter.toDTO(reservation.getVehicule()));
        return dto;
    }

    public static @NonNull ReservationVehicule toModel(ReserverVehiculeServiceDTO dto, Utilisateur utilisateur, Vehicule vehicule) {
        ReservationVehicule reservation = new ReservationVehicule();
        reservation.setDateHeureDebut(dto.getDateHeureDebut());
        reservation.setDateHeureFin(dto.getDateHeureFin());
        reservation.setUtilisateur(utilisateur);
        reservation.setVehicule(vehicule);
        reservation.setStatutReservation(StatutReservation.PAS_COMMENCEE);
        return reservation;
    }
}
