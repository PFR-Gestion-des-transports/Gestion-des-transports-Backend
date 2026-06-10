package gestiontransports.model;

import gestiontransports.enums.StatutReservation;
import gestiontransports.interfaces.OwnedByUtilisateur;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité JPA représentant la réservation d'un véhicule de service par un collaborateur.
 * Une réservation couvre une plage horaire (minimum 1 jour, maximum 1 semaine) et évolue
 * à travers les statuts définis par {@link gestiontransports.enums.StatutReservation}.
 */
@Entity
@Table(name = "ReservationVehicule")
public class ReservationVehicule implements OwnedByUtilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "DateHeureDebut", nullable = false)
    private LocalDateTime dateHeureDebut;

    @Column(name = "DateHeureFin", nullable = false)
    private LocalDateTime dateHeureFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "StatutReservation", nullable = false)
    private StatutReservation statutReservation;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;

    public ReservationVehicule() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDateTime getDateHeureDebut() { return dateHeureDebut; }
    public void setDateHeureDebut(LocalDateTime dateHeureDebut) { this.dateHeureDebut = dateHeureDebut; }

    public LocalDateTime getDateHeureFin() { return dateHeureFin; }
    public void setDateHeureFin(LocalDateTime dateHeureFin) { this.dateHeureFin = dateHeureFin; }

    public StatutReservation getStatutReservation() { return statutReservation; }
    public void setStatutReservation(StatutReservation statutReservation) { this.statutReservation = statutReservation; }

    @Override
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public Vehicule getVehicule() { return vehicule; }
    public void setVehicule(Vehicule vehicule) { this.vehicule = vehicule; }
}
