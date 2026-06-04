package gestiontransports.dto.vehicule;

import gestiontransports.enums.Categorie;
import gestiontransports.enums.Motorisation;
import gestiontransports.enums.StatutVehicule;

/**
 * DTO de sortie représentant un véhicule exposé dans les réponses de l'API.
 */
public class VehiculeDTO {

    private Integer id;
    private String immatriculation;
    private String marque;
    private String modele;
    /** URL de la photo du véhicule. */
    private String urlPhoto;
    /** Émissions de CO2 en grammes par kilomètre. */
    private double co2Km;
    private int nombreDePlace;
    private Categorie categorie;
    private Motorisation motorisation;
    /** Indique si le véhicule est un véhicule de service (true) ou personnel (false). */
    private boolean estVehiculeService;
    private StatutVehicule statutVehicule;
    /** Identifiant de l'utilisateur propriétaire du véhicule, null si véhicule de service. */
    private Integer utilisateurId;

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

    public Integer getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(Integer utilisateurId) { this.utilisateurId = utilisateurId; }
}
