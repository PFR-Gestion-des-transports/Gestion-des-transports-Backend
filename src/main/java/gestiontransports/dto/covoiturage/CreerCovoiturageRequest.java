package gestiontransports.dto.covoiturage;

import java.time.LocalDateTime;

import gestiontransports.dto.adresse.AdresseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CreerCovoiturageRequest {

    @NotNull
    private Integer nbrPlaceInitial;

    @NotNull
    private LocalDateTime dateHeureDebut;

    @NotNull
    @Valid
    private AdresseDTO adresseDepart;

    @NotNull
    @Valid
    private AdresseDTO adresseArrivee;

    public Integer getNbrPlaceInitial() { return nbrPlaceInitial; }
    public void setNbrPlaceInitial(Integer nbrPlaceInitial) { this.nbrPlaceInitial = nbrPlaceInitial; }

    public LocalDateTime getDateHeureDebut() { return dateHeureDebut; }
    public void setDateHeureDebut(LocalDateTime dateHeureDebut) { this.dateHeureDebut = dateHeureDebut; }

    public AdresseDTO getAdresseDepart() { return adresseDepart; }
    public void setAdresseDepart(AdresseDTO adresseDepart) { this.adresseDepart = adresseDepart; }

    public AdresseDTO getAdresseArrivee() { return adresseArrivee; }
    public void setAdresseArrivee(AdresseDTO adresseArrivee) { this.adresseArrivee = adresseArrivee; }
}
