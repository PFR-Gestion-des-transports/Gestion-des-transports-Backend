package gestiontransports.dto.vehicule;

import gestiontransports.enums.Categorie;
import gestiontransports.enums.Motorisation;
import gestiontransports.enums.StatutVehicule;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;


/**
 * DTO d'entrée pour la modification complète d'un véhicule existant,
 * reçu via PUT /vehicules/{id}.
 */
public class ModifierVehiculeRequestDTO {

    @NotBlank
    private String immatriculation;

    @NotBlank
    private String marque;

    @NotBlank
    private String modele;

    @NotBlank
    /** URL de la photo du véhicule. */
    private String urlPhoto;

    @PositiveOrZero
    /** Émissions de CO2 en grammes par kilomètre. */
    private double co2Km;

    @NotNull
    @Min(2)
    private Integer nombreDePlace;

    @NotNull
    private Categorie categorie;

    @NotNull
    private Motorisation motorisation;

    @NotNull
    private StatutVehicule statutVehicule;

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

    public StatutVehicule getStatutVehicule() { return statutVehicule; }
    public void setStatutVehicule(StatutVehicule statutVehicule) { this.statutVehicule = statutVehicule; }
}
