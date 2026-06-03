package gestiontransports.dto.reservationvehicule;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ReserverVehiculeServiceDTO {

    @NotNull
    private LocalDateTime dateHeureDebut;

    @NotNull
    private LocalDateTime dateHeureFin;

    @NotNull
    private Integer utilisateurId;

    @NotNull
    private Integer vehiculeId;

    public LocalDateTime getDateHeureDebut() { return dateHeureDebut; }
    public void setDateHeureDebut(LocalDateTime dateHeureDebut) { this.dateHeureDebut = dateHeureDebut; }

    public LocalDateTime getDateHeureFin() { return dateHeureFin; }
    public void setDateHeureFin(LocalDateTime dateHeureFin) { this.dateHeureFin = dateHeureFin; }

    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }

    public int getVehiculeId() { return vehiculeId; }
    public void setVehiculeId(int vehiculeId) { this.vehiculeId = vehiculeId; }
}
