package gestiontransports.dto.reservationvehicule;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO d'entrée pour la modification d'une réservation de véhicule de service.
 * Contient les nouvelles dates de début et de fin souhaitées par le collaborateur,
 * toutes deux obligatoires et soumises aux règles métier (min 1 jour, max 1 semaine).
 */
public class ModifierReservationVehiculeDTO {

    @NotNull
    private LocalDateTime dateHeureDebut;

    @NotNull
    private LocalDateTime dateHeureFin;

    public LocalDateTime getDateHeureDebut() { return dateHeureDebut; }
    public void setDateHeureDebut(LocalDateTime dateHeureDebut) { this.dateHeureDebut = dateHeureDebut; }

    public LocalDateTime getDateHeureFin() { return dateHeureFin; }
    public void setDateHeureFin(LocalDateTime dateHeureFin) { this.dateHeureFin = dateHeureFin; }
}
