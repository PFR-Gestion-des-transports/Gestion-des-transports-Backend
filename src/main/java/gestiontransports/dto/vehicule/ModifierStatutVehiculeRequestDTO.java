package gestiontransports.dto.vehicule;

import gestiontransports.enums.StatutVehicule;
import jakarta.validation.constraints.NotNull;

/**
 * DTO d'entrée pour la modification du seul statut d'un véhicule,
 * reçu via PATCH /vehicules/{id}/statut.
 */
public class ModifierStatutVehiculeRequestDTO {

    @NotNull
    private StatutVehicule statutVehicule;

    public StatutVehicule getStatutVehicule() { return statutVehicule; }
    public void setStatutVehicule(StatutVehicule statutVehicule) { this.statutVehicule = statutVehicule; }
}
