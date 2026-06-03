package gestiontransports.dto.covoiturage;

import java.time.LocalDateTime;

import gestiontransports.dto.adresse.AdresseInputDTO;
import gestiontransports.enums.StatutCovoiturage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class ModifierCovoiturageDTO {
    
    @NotNull
    private Integer nbrPlaceInitial;

    @NotNull
    private Integer nbrPlaceRestante;

    @NotNull
    private LocalDateTime dateHeureDebut;

    @NotNull
    @Valid
    private AdresseInputDTO adresseDepart;

    @NotNull
    @Valid
    private AdresseInputDTO adresseArrivee;

    @NotNull
    private Integer vehiculeId;

    @NotNull
    private StatutCovoiturage statut;

    public Integer getNbrPlaceInitial() { return nbrPlaceInitial; }
    public void setNbrPlaceInitial(Integer nbrPlaceInitial) { this.nbrPlaceInitial = nbrPlaceInitial; }

    public Integer getNbrPlaceRestante() { return nbrPlaceRestante; }
    public void setNbrPlaceRestante(Integer nbrPlaceRestante) { this.nbrPlaceRestante = nbrPlaceRestante; }

    public LocalDateTime getDateHeureDebut() { return dateHeureDebut; }
    public void setDateHeureDebut(LocalDateTime dateHeureDebut) { this.dateHeureDebut = dateHeureDebut; }

    public AdresseInputDTO getAdresseDepart() { return adresseDepart; }
    public void setAdresseDepart(AdresseInputDTO adresseDepart) { this.adresseDepart = adresseDepart; }

    public AdresseInputDTO getAdresseArrivee() { return adresseArrivee; }
    public void setAdresseArrivee(AdresseInputDTO adresseArrivee) { this.adresseArrivee = adresseArrivee; }

    public Integer getVehiculeId() { return vehiculeId; }
    public void setVehiculeId(Integer vehiculeId) { this.vehiculeId = vehiculeId; }

    public StatutCovoiturage getStatut() { return statut; }
    public void setStatut(StatutCovoiturage statut) { this.statut = statut; }
}
