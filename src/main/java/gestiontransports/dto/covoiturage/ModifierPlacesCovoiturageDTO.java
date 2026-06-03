package gestiontransports.dto.covoiturage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO d'entrée pour la mise à jour du nombre de places restantes d'un covoiturage,
 * reçu via PATCH /covoiturages/{id}/places.
 * Permet au conducteur de réduire manuellement les places disponibles
 * (par exemple si un passager l'a rejoint hors application).
 */
public class ModifierPlacesCovoiturageDTO {

    @NotNull
    @Min(0)
    /** Nouveau nombre de places disponibles à la réservation. Doit être compris entre 0 et nbrPlaceInitial. */
    private Integer nbrPlaceRestante;

    public Integer getNbrPlaceRestante() { return nbrPlaceRestante; }
    public void setNbrPlaceRestante(Integer nbrPlaceRestante) { this.nbrPlaceRestante = nbrPlaceRestante; }
}
