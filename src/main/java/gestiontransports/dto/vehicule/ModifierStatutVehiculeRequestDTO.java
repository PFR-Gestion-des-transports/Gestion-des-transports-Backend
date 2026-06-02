package gestiontransports.dto.vehicule;

import gestiontransports.enums.StatutVehicule;
import jakarta.validation.constraints.NotNull;

public class ModifierStatutVehiculeRequestDTO {

    @NotNull
    private StatutVehicule statutVehicule;

    public StatutVehicule getStatutVehicule() { return statutVehicule; }
    public void setStatutVehicule(StatutVehicule statutVehicule) { this.statutVehicule = statutVehicule; }
}
