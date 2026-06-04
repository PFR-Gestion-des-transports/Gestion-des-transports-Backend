package gestiontransports.enums;

/**
 * Enumération des statuts possibles d'une réservation de véhicule de service.
 * Le cycle de vie normal d'une réservation est : {@code PAS_COMMENCEE} → {@code COMMENCEE} → {@code TERMINEE}.
 * Une réservation peut être annulée à tout moment avant son démarrage effectif.
 */
public enum StatutReservation {

    /** La réservation est en cours : le collaborateur utilise actuellement le véhicule. */
    COMMENCEE,

    /** La réservation a été annulée avant son démarrage. */
    ANNULEE,

    /** La réservation est terminée : le véhicule a été restitué. */
    TERMINEE,

    /** La réservation est enregistrée mais n'a pas encore débuté. */
    PAS_COMMENCEE
}
