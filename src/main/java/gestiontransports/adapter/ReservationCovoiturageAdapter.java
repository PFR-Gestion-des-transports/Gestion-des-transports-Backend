package gestiontransports.adapter;

import gestiontransports.model.ReservationCovoiturage;
import gestiontransports.dto.reservation.ReservationCovoiturageDTO;

/**
 * Adaptateur de conversion entre l'entité {@link ReservationCovoiturage} et son DTO de sortie.
 * Agrège les informations du covoiturage et du passager dans un seul objet de réponse.
 */
public class ReservationCovoiturageAdapter {

    /**
     * Convertit une entité {@link ReservationCovoiturage} en {@link ReservationCovoiturageDTO}.
     * Mappe les champs : id, covoiturage (via {@link CovoiturageAdapter#toDTO}) et
     * utilisateur (via {@link UtilisateurAdapter#toDTO}).
     *
     * @param reservation l'entité réservation à convertir
     * @return le DTO de sortie correspondant
     */
    public static ReservationCovoiturageDTO toDTO(ReservationCovoiturage reservation) {
        ReservationCovoiturageDTO dto = new ReservationCovoiturageDTO();
        dto.setId(reservation.getId());
        dto.setCovoiturage(CovoiturageAdapter.toDTO(reservation.getCovoiturage()));
        dto.setUtilisateur(UtilisateurAdapter.toDTO(reservation.getUtilisateur()));
        return dto;
    }
}
