package gestiontransports.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

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

    public ReservationCovoiturage() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Covoiturage getCovoiturage() { return covoiturage; }
    public void setCovoiturage(Covoiturage covoiturage) { this.covoiturage = covoiturage; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

}
