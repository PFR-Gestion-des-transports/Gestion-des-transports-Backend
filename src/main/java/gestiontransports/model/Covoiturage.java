package gestiontransports.model;
import java.util.HashSet;
import java.util.Set;

import gestiontransports.enums.StatutCovoiturage;
import gestiontransports.interfaces.OwnedByUtilisateur;
import java.time.LocalDateTime;
import jakarta.persistence.*;

/**
 * Entité représentant un trajet de covoiturage d'entreprise, avec ses adresses de départ et
 * d'arrivée, ses places disponibles, sa date de départ, le conducteur et le véhicule utilisé.
 * Un covoiturage peut avoir plusieurs réservations et évolue à travers différents statuts
 * (ex. : OUVERT, EN_COURS, TERMINE, ANNULE).
 */
@Entity
@Table(name = "Covoiturage")
public class Covoiturage implements OwnedByUtilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nbrPlaceInitial", nullable = false, length = 100)
    private Integer nbrPlaceInitial;

    @Column(name = "nbrPlaceRestante", nullable = false, length = 150)
    private Integer nbrPlaceRestante;

    @Column(name = "dateHeureDebut", nullable = false, length = 10)
    private LocalDateTime dateHeureDebut;

    @ManyToOne
    @JoinColumn(name = "adresseDepart", nullable = false)
    private Adresse adresseDepart;

    @ManyToOne
    @JoinColumn(name = "adresseArrivee", nullable = false)
    private Adresse adresseArrivee;

    @OneToMany(mappedBy = "covoiturage", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReservationCovoiturage> reservations = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "utilisateur", nullable = false)
    private Utilisateur utilisateur;

    @Enumerated(EnumType.STRING)
    @Column(name = "Statut", nullable = false, length = 50)
    private StatutCovoiturage statut;

    @ManyToOne
    @JoinColumn(name = "vehicule", nullable = false)
    private Vehicule vehicule;



    public Covoiturage() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getNbrPlaceInitial() { return nbrPlaceInitial; }
    public void setNbrPlaceInitial(Integer nbrPlaceInitial) { this.nbrPlaceInitial = nbrPlaceInitial; }

    public Integer getNbrPlaceRestante() { return nbrPlaceRestante; }
    public void setNbrPlaceRestante(Integer nbrPlaceRestante) { this.nbrPlaceRestante = nbrPlaceRestante; }

    public LocalDateTime getDateHeureDebut() { return dateHeureDebut; }
    public void setDateHeureDebut(LocalDateTime dateHeureDebut) { this.dateHeureDebut = dateHeureDebut; }

    public Adresse getAdresseDepart() { return adresseDepart; }
    public void setAdresseDepart(Adresse adresseDepart) { this.adresseDepart = adresseDepart; }

    public Adresse getAdresseArrivee() { return adresseArrivee; }
    public void setAdresseArrivee(Adresse adresseArrivee) { this.adresseArrivee = adresseArrivee; }

    public Set<ReservationCovoiturage> getReservations() { return reservations; }
    public void setReservations(Set<ReservationCovoiturage> reservations) { this.reservations = reservations; } 

    @Override
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public StatutCovoiturage getStatut() { return statut; }
    public void setStatut(StatutCovoiturage statut) { this.statut = statut; }

    public Vehicule getVehicule() { return vehicule; }
    public void setVehicule(Vehicule vehicule) { this.vehicule = vehicule; }
}
