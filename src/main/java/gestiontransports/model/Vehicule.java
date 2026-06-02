package gestiontransports.model;

import gestiontransports.enums.Categorie;
import gestiontransports.enums.Motorisation;
import gestiontransports.enums.StatutVehicule;
import jakarta.persistence.*;
import java.math.BigDecimal;

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

    @Column(name = "CO2KM", nullable = false, precision = 10, scale = 2)
    private BigDecimal co2Km;

    @Column(name = "NombreDePlace", nullable = false)
    private Integer nombreDePlace;

    @Enumerated(EnumType.STRING)
    @Column(name = "Categorie", nullable = false, length = 50)
    private Categorie categorie;

    @Enumerated(EnumType.STRING)
    @Column(name = "Motorisation", nullable = false, length = 20)
    private Motorisation motorisation;

    @Column(name = "EstVehiculeService", nullable = false)
    private Boolean estVehiculeService = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "StatutVehicule", nullable = false, length = 20)
    private StatutVehicule statutVehicule;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

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

    public BigDecimal getCo2Km() { return co2Km; }
    public void setCo2Km(BigDecimal co2Km) { this.co2Km = co2Km; }

    public Integer getNombreDePlace() { return nombreDePlace; }
    public void setNombreDePlace(Integer nombreDePlace) { this.nombreDePlace = nombreDePlace; }

    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }

    public Motorisation getMotorisation() { return motorisation; }
    public void setMotorisation(Motorisation motorisation) { this.motorisation = motorisation; }

    public Boolean getEstVehiculeService() { return estVehiculeService; }
    public void setEstVehiculeService(Boolean estVehiculeService) { this.estVehiculeService = estVehiculeService; }

    public StatutVehicule getStatutVehicule() { return statutVehicule; }
    public void setStatutVehicule(StatutVehicule statutVehicule) { this.statutVehicule = statutVehicule; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
}
