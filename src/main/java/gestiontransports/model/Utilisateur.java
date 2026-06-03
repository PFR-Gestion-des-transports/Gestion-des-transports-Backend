package gestiontransports.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gestiontransports.enums.Role;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Prenom", nullable = false, length = 50)
    private String prenom;

    @Column(name = "Nom", nullable = false, length = 50)
    private String nom;

    @Column(name = "Email", nullable = false, unique = true, length = 255)
    private String email;

    @JsonIgnore
    @Column(name = "MotDePasse", nullable = false, length = 60)
    private String motDePasse;

    @ManyToOne
    @JoinColumn(name = "adresse_id", nullable = false)
    private Adresse adresse;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReservationCovoiturage> reservations = new HashSet<>();

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Covoiturage> covoiturages = new HashSet<>();

    @OneToMany(mappedBy = "utilisateur")
    private List<Vehicule> vehicules = new ArrayList<>();

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationVehicule> reservationsVehicule = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "utilisateur_roles", joinColumns = @JoinColumn(name = "utilisateur_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    public Utilisateur() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public Adresse getAdresse() { return adresse; }
    public void setAdresse(Adresse adresse) { this.adresse = adresse; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public List<Vehicule> getVehicules() { return vehicules; }
    public void setVehicules(List<Vehicule> vehicules) { this.vehicules = vehicules; }

    public List<ReservationVehicule> getReservationsVehicule() { return reservationsVehicule; }
    public void setReservationsVehicule(List<ReservationVehicule> reservationsVehicule) { this.reservationsVehicule = reservationsVehicule; }
}
