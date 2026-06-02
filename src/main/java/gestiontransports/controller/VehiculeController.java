package gestiontransports.controller;

import gestiontransports.dto.vehicule.CreerVehiculeRequestDTO;
import gestiontransports.dto.vehicule.ModifierStatutVehiculeRequestDTO;
import gestiontransports.dto.vehicule.ModifierVehiculeRequestDTO;
import gestiontransports.dto.vehicule.VehiculeDTO;
import gestiontransports.service.VehiculeService;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicules")
public class VehiculeController {

    private final VehiculeService vehiculeService;

    public VehiculeController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<VehiculeDTO>> findVehiculesDisponibles() {
        return ResponseEntity.ok(vehiculeService.findVehiculesDisponibles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(vehiculeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<VehiculeDTO> create(@Valid @RequestBody CreerVehiculeRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeService.create(request, false));
    }

    @PostMapping("/service")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<VehiculeDTO> createService(@Valid @RequestBody CreerVehiculeRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeService.create(request, true));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculeDTO> update(@PathVariable Integer id,
                                               @Valid @RequestBody ModifierVehiculeRequestDTO request) {
        return ResponseEntity.ok(vehiculeService.update(id, request));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<VehiculeDTO> modifierStatut(@PathVariable Integer id,
                                                       @Valid @RequestBody ModifierStatutVehiculeRequestDTO request) {
        return ResponseEntity.ok(vehiculeService.modifierStatut(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        vehiculeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
