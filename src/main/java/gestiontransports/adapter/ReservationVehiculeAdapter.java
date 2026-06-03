package gestiontransports.adapter;

import gestiontransports.dto.reservationvehicule.ReservationVehiculeDTO;
import gestiontransports.dto.reservationvehicule.ReserverVehiculeServiceDTO;
import gestiontransports.enums.StatutReservation;
import gestiontransports.model.ReservationVehicule;
import gestiontransports.model.Utilisateur;
import gestiontransports.model.Vehicule;
import org.springframework.lang.NonNull;

/**
 * Adaptateur de conversion entre l'entité {@link ReservationVehicule} et ses DTOs associés.
 * Centralise la logique de mapping pour éviter toute dépendance directe aux entités JPA
 * dans les couches controller et service.
 */
public class ReservationVehiculeAdapter {

    /**
     * Convertit une entité {@link ReservationVehicule} en DTO de sortie.
     *
     * @param reservation l'entité à convertir
     * @return le DTO représentant la réservation
     */
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

    /**
     * Construit une nouvelle entité {@link ReservationVehicule} à partir du DTO de création,
     * du collaborateur et du véhicule cibles. Le statut est initialisé à {@code PAS_COMMENCEE}.
     *
     * @param dto        le DTO contenant les dates souhaitées
     * @param utilisateur le collaborateur effectuant la réservation
     * @param vehicule   le véhicule réservé
     * @return l'entité prête à être persistée
     */
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
