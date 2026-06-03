package gestiontransports.dto.reservationvehicule;

import gestiontransports.dto.utilisateur.UtilisateurDTO;
import gestiontransports.dto.vehicule.VehiculeDTO;
import gestiontransports.enums.StatutReservation;

import java.time.LocalDateTime;

/**
 * DTO de sortie représentant une réservation de véhicule de service.
 * Expose l'ensemble des informations d'une réservation (plage horaire, statut, collaborateur
 * et véhicule associés) pour les réponses de l'API REST.
 */
public class ReservationVehiculeDTO {

    private int id;
    private LocalDateTime dateHeureDebut;
    private LocalDateTime dateHeureFin;
    private StatutReservation statutReservation;
    private UtilisateurDTO utilisateur;
    private VehiculeDTO vehicule;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getDateHeureDebut() { return dateHeureDebut; }
    public void setDateHeureDebut(LocalDateTime dateHeureDebut) { this.dateHeureDebut = dateHeureDebut; }

    public LocalDateTime getDateHeureFin() { return dateHeureFin; }
    public void setDateHeureFin(LocalDateTime dateHeureFin) { this.dateHeureFin = dateHeureFin; }

    public StatutReservation getStatutReservation() { return statutReservation; }
    public void setStatutReservation(StatutReservation statutReservation) { this.statutReservation = statutReservation; }

    public UtilisateurDTO getUtilisateur() { return utilisateur; }
    public void setUtilisateur(UtilisateurDTO utilisateur) { this.utilisateur = utilisateur; }

    public VehiculeDTO getVehicule() { return vehicule; }
    public void setVehicule(VehiculeDTO vehicule) { this.vehicule = vehicule; }
}
