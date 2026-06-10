package gestiontransports.model;

import gestiontransports.enums.StatutReservation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.EnumType;

/**
 * Entité de liaison représentant la réservation d'une place dans un covoiturage par un utilisateur passager.
 * Elle établit la relation many-to-many entre {@link Utilisateur} et {@link Covoiturage}.
 */
@Entity
@Table(name = "ReservationCovoiturage")
public class ReservationCovoiturage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "covoiturage", nullable = false)
    private Covoiturage covoiturage;
    
    @ManyToOne
    @JoinColumn(name = "utilisateur", nullable = false)
    private Utilisateur utilisateur;

    @Enumerated(EnumType.STRING)
    @Column(name = "Statut", nullable = false, length = 50)
    private StatutReservation statut;

    public ReservationCovoiturage() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Covoiturage getCovoiturage() { return covoiturage; }
    public void setCovoiturage(Covoiturage covoiturage) { this.covoiturage = covoiturage; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public StatutReservation getStatut() { return statut; }
    public void setStatut(StatutReservation statut) { this.statut = statut; }

}
