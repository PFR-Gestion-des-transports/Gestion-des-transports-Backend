package gestiontransports.enums;

/**
 * Statut représentant l'étape du cycle de vie d'un covoiturage.
 */
public enum StatutCovoiturage {

    /** Le covoiturage est actuellement en train de se dérouler. */
    EN_COURS,

    /** Le covoiturage s'est achevé normalement. */
    TERMINE,

    /** Le covoiturage a été annulé avant ou pendant son déroulement. */
    ANNULE,

    /** Le covoiturage est planifié mais n'a pas encore débuté. */
    PAS_COMMENCER
}
