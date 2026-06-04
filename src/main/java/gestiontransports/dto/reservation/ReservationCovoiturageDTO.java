package gestiontransports.dto.reservation;

import gestiontransports.dto.covoiturage.CovoiturageDTO;
import gestiontransports.dto.utilisateur.UtilisateurDTO;
import gestiontransports.enums.StatutReservation;;

/**
 * DTO de sortie représentant une réservation de covoiturage, retourné par les endpoints
 * de consultation et de création de réservation.
 */
public class ReservationCovoiturageDTO {

    private int id;

    private CovoiturageDTO covoiturage;

    private UtilisateurDTO utilisateur;

    private StatutReservation statut;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public CovoiturageDTO getCovoiturage() { return covoiturage; }
    public void setCovoiturage(CovoiturageDTO covoiturage) { this.covoiturage = covoiturage; }

    public UtilisateurDTO getUtilisateur() { return utilisateur; }
    public void setUtilisateur(UtilisateurDTO utilisateur) { this.utilisateur = utilisateur; }

    public StatutReservation getStatut() {  return statut; }
    public void setStatut(StatutReservation statut) { this.statut = statut; }

}
