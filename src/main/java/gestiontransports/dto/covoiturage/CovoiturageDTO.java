package gestiontransports.dto.covoiturage;

import java.time.LocalDateTime;
import gestiontransports.dto.adresse.AdresseOutputDTO;

public class CovoiturageDTO {

    private int id;

    private Integer nbrPlaceInitial;
    private LocalDateTime dateHeureDebut;
    private AdresseOutputDTO adresseDepart;
    private AdresseOutputDTO adresseArrivee;

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
}
