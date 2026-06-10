package gestiontransports.enums;

/**
 * Catégorie d'un véhicule selon sa taille et son usage.
 */
public enum Categorie {

    /** Véhicule de très petite taille, conçu pour un usage exclusivement urbain. */
    MICRO_URBAINES,

    /** Petite citadine légèrement plus grande que la micro-urbaine. */
    MINI_CITADINES,

    /** Citadine offrant un compromis entre compacité et polyvalence. */
    CITADINES_POLYVALENTES,

    /** Véhicule compact adapté à la ville comme aux trajets mixtes. */
    COMPACTES,

    /** Berline de taille petite (segment S). */
    BERLINES_TAILLE_S,

    /** Berline de taille moyenne (segment M). */
    BERLINES_TAILLE_M,

    /** Berline de grande taille (segment L). */
    BERLINES_TAILLE_L,

    /** Véhicule de type SUV (Sport Utility Vehicle), surélevé et polyvalent. */
    SUV,

    /** Véhicule conçu pour la conduite hors route et les terrains difficiles. */
    TOUT_TERRAINS,

    /** Véhicule utilitaire à plateau découvert, adapté au transport de charges. */
    PICK_UP
}
