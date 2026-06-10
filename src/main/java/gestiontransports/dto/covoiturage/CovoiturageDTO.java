package gestiontransports.dto.covoiturage;

import java.time.LocalDateTime;
import gestiontransports.dto.adresse.AdresseOutputDTO;
import gestiontransports.enums.StatutCovoiturage;

/**
 * DTO de sortie représentant un covoiturage, retourné par les endpoints de consultation
 * et de création de covoiturage.
 */
public class CovoiturageDTO {

    private int id;

    /** Nombre de places disponibles à la création du covoiturage. */
    private Integer nbrPlaceInitial;
    private LocalDateTime dateHeureDebut;
    private AdresseOutputDTO adresseDepart;
    private AdresseOutputDTO adresseArrivee;
    private StatutCovoiturage statut;
    /** Identifiant du véhicule assigné à ce covoiturage. */
    private int vehiculeId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getNbrPlaceInitial() { return nbrPlaceInitial; }
    public void setNbrPlaceInitial(Integer nbrPlaceInitial) { this.nbrPlaceInitial = nbrPlaceInitial; }

    public LocalDateTime getDateHeureDebut() { return dateHeureDebut; }
    public void setDateHeureDebut(LocalDateTime dateHeureDebut) { this.dateHeureDebut = dateHeureDebut; }

    public AdresseOutputDTO getAdresseDepart() { return adresseDepart; }
    public void setAdresseDepart(AdresseOutputDTO adresseDepart) { this.adresseDepart = adresseDepart; }

    public AdresseOutputDTO getAdresseArrivee() { return adresseArrivee; }
    public void setAdresseArrivee(AdresseOutputDTO adresseArrivee) { this.adresseArrivee = adresseArrivee; }

    public int getVehiculeId() { return vehiculeId; }
    public void setVehiculeId(int vehiculeId) { this.vehiculeId = vehiculeId; }

    public StatutCovoiturage getStatut() { return statut; }
    public void setStatut(StatutCovoiturage statut) { this.statut = statut; }
}
