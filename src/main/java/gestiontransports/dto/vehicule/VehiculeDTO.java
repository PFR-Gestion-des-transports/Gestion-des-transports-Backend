package gestiontransports.dto.vehicule;

import gestiontransports.enums.Categorie;
import gestiontransports.enums.Motorisation;
import gestiontransports.enums.StatutVehicule;

public class VehiculeDTO {

    private Integer id;
    private String immatriculation;
    private String marque;
    private String modele;
    private String urlPhoto;
    private double co2Km;
    private Integer nombreDePlace;
    private Categorie categorie;
    private Motorisation motorisation;
    private Boolean estVehiculeService;
    private StatutVehicule statutVehicule;
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

    public Integer getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(Integer utilisateurId) { this.utilisateurId = utilisateurId; }
}
