package gestiontransports.dto.covoiturage;

import java.time.LocalDateTime;

import gestiontransports.dto.adresse.AdresseInputDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * DTO d'entrée pour la création d'un covoiturage, reçu via POST /covoiturages.
 */
public class CreerCovoiturageRequest {

    @NotNull
    /** Nombre de places proposées lors de la création du covoiturage. */
    private Integer nbrPlaceInitial;

    @NotNull
    private LocalDateTime dateHeureDebut;

    @NotNull
    @Valid
    private AdresseInputDTO adresseDepart;

    @NotNull
    @Valid
    private AdresseInputDTO adresseArrivee;

    @NotNull
    /** Identifiant du véhicule à associer au covoiturage. */
    private Integer vehiculeId;

    public Integer getNbrPlaceInitial() { return nbrPlaceInitial; }
    public void setNbrPlaceInitial(Integer nbrPlaceInitial) { this.nbrPlaceInitial = nbrPlaceInitial; }

    public LocalDateTime getDateHeureDebut() { return dateHeureDebut; }
    public void setDateHeureDebut(LocalDateTime dateHeureDebut) { this.dateHeureDebut = dateHeureDebut; }

    public AdresseInputDTO getAdresseDepart() { return adresseDepart; }
    public void setAdresseDepart(AdresseInputDTO adresseDepart) { this.adresseDepart = adresseDepart; }

    public AdresseInputDTO getAdresseArrivee() { return adresseArrivee; }
    public void setAdresseArrivee(AdresseInputDTO adresseArrivee) { this.adresseArrivee = adresseArrivee; }

    public Integer getVehiculeId() { return vehiculeId; }
    public void setVehiculeId(Integer vehiculeId) { this.vehiculeId = vehiculeId; }
}
