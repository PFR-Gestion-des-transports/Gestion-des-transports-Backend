package gestiontransports.dto.reservation;

import gestiontransports.dto.covoiturage.CovoiturageDTO;
import gestiontransports.dto.utilisateur.UtilisateurDTO;

public class ReservationCovoiturageDTO {
    
    private Integer id;

    private CovoiturageDTO covoiturage;

    private UtilisateurDTO utilisateur;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public CovoiturageDTO getCovoiturage() { return covoiturage; }
    public void setCovoiturage(CovoiturageDTO covoiturage) { this.covoiturage = covoiturage; }
    
    public UtilisateurDTO getUtilisateur() { return utilisateur; }
    public void setUtilisateur(UtilisateurDTO utilisateur) { this.utilisateur = utilisateur; }

}
