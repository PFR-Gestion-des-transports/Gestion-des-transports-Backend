package gestiontransports.dto.reservationvehicule;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO d'entrée pour la réservation d'un véhicule de service.
 * L'utilisateur est identifié via le token JWT — aucun identifiant utilisateur n'est requis dans le corps.
 */
public class ReserverVehiculeServiceDTO {

    @NotNull
    private LocalDateTime dateHeureDebut;

    @NotNull
    private LocalDateTime dateHeureFin;

    @NotNull
    private Integer vehiculeId;

    public LocalDateTime getDateHeureDebut() { return dateHeureDebut; }
    public void setDateHeureDebut(LocalDateTime dateHeureDebut) { this.dateHeureDebut = dateHeureDebut; }

    public LocalDateTime getDateHeureFin() { return dateHeureFin; }
    public void setDateHeureFin(LocalDateTime dateHeureFin) { this.dateHeureFin = dateHeureFin; }

    public Integer getVehiculeId() { return vehiculeId; }
    public void setVehiculeId(Integer vehiculeId) { this.vehiculeId = vehiculeId; }
}
