package gestiontransports.dto.reservationvehicule;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
