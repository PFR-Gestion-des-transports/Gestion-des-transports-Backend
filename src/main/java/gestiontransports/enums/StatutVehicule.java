package gestiontransports.enums;

/**
 * Statut représentant l'état opérationnel d'un véhicule.
 */
public enum StatutVehicule {

    /** Le véhicule est définitivement ou temporairement retiré de la flotte et ne peut pas être utilisé. */
    HORS_SERVICE,

    /** Le véhicule est disponible et opérationnel pour effectuer des trajets. */
    EN_SERVICE,

    /** Le véhicule est immobilisé pour cause de maintenance ou de réparation. */
    EN_REPARATION
}
