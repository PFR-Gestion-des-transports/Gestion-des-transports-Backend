package gestiontransports.dto.reservation;

import gestiontransports.dto.covoiturage.CovoiturageDTO;
import gestiontransports.dto.utilisateur.UtilisateurDTO;

public class ReservationCovoiturageDTO {

    private int id;

    private CovoiturageDTO covoiturage;

    private UtilisateurDTO utilisateur;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public CovoiturageDTO getCovoiturage() { return covoiturage; }
    public void setCovoiturage(CovoiturageDTO covoiturage) { this.covoiturage = covoiturage; }
    
    public UtilisateurDTO getUtilisateur() { return utilisateur; }
    public void setUtilisateur(UtilisateurDTO utilisateur) { this.utilisateur = utilisateur; }

}
