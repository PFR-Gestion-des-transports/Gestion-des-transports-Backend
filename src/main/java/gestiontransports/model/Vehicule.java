package gestiontransports.model;

import gestiontransports.enums.Categorie;
import gestiontransports.enums.Motorisation;
import gestiontransports.enums.StatutVehicule;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "Vehicule")
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Immatriculation", nullable = false, unique = true, length = 20)
    private String immatriculation;

    @Column(name = "Marque", nullable = false, length = 50)
    private String marque;

    @Column(name = "Modele", nullable = false, length = 50)
    private String modele;

    @Column(name = "UrlPhoto", nullable = false, length = 500)
    private String urlPhoto;

    @Column(name = "CO2KM", nullable = false)
    private double co2Km;

    @Column(name = "NombreDePlace", nullable = false)
    private int nombreDePlace;

    @Enumerated(EnumType.STRING)
    @Column(name = "Categorie", nullable = false, length = 50)
    private Categorie categorie;

    @Enumerated(EnumType.STRING)
    @Column(name = "Motorisation", nullable = false, length = 20)
    private Motorisation motorisation;

    @Column(name = "EstVehiculeService", nullable = false)
    private boolean estVehiculeService;

    @Enumerated(EnumType.STRING)
    @Column(name = "StatutVehicule", nullable = false, length = 20)
    private StatutVehicule statutVehicule;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Covoiturage> covoiturages = new ArrayList<>();

    public Vehicule() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }

    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }

    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }

    public String getUrlPhoto() { return urlPhoto; }
    public void setUrlPhoto(String urlPhoto) { this.urlPhoto = urlPhoto; }

    public double getCo2Km() { return co2Km; }
    public void setCo2Km(double co2Km) { this.co2Km = co2Km; }

    public int getNombreDePlace() { return nombreDePlace; }
    public void setNombreDePlace(int nombreDePlace) { this.nombreDePlace = nombreDePlace; }

    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }

    public Motorisation getMotorisation() { return motorisation; }
    public void setMotorisation(Motorisation motorisation) { this.motorisation = motorisation; }

    public boolean isEstVehiculeService() { return estVehiculeService; }
    public void setEstVehiculeService(boolean estVehiculeService) { this.estVehiculeService = estVehiculeService; }

    public StatutVehicule getStatutVehicule() { return statutVehicule; }
    public void setStatutVehicule(StatutVehicule statutVehicule) { this.statutVehicule = statutVehicule; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public List<Covoiturage> getCovoiturages() { return covoiturages; }
    public void setCovoiturages(List<Covoiturage> covoiturages) { this.covoiturages = covoiturages; }
}
