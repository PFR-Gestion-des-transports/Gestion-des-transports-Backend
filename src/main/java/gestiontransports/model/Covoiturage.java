package gestiontransports.model;
import java.util.HashSet;
import java.util.Set;

import gestiontransports.enums.Categorie;
import gestiontransports.enums.StatutCovoiturage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.CascadeType;
import java.time.LocalDateTime;

@Entity
@Table(name = "Covoiturage")
public class Covoiturage {

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

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public StatutCovoiturage getStatut() { return statut; }
    public void setStatut(StatutCovoiturage statut) { this.statut = statut; }
}
