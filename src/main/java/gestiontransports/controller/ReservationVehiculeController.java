package gestiontransports.controller;

import gestiontransports.dto.reservationvehicule.ModifierReservationVehiculeDTO;
import gestiontransports.dto.reservationvehicule.ReservationVehiculeDTO;
import gestiontransports.dto.reservationvehicule.ReserverVehiculeServiceDTO;
import gestiontransports.service.ReservationVehiculeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations-vehicules")
public class ReservationVehiculeController {

    private final ReservationVehiculeService reservationVehiculeService;

    public ReservationVehiculeController(ReservationVehiculeService reservationVehiculeService) {
        this.reservationVehiculeService = reservationVehiculeService;
    }

    @GetMapping("/vehicule/{vehiculeId}")
    public ResponseEntity<List<ReservationVehiculeDTO>> findActivesByVehiculeId(@PathVariable int vehiculeId) {
        return ResponseEntity.ok(reservationVehiculeService.findActivesByVehiculeId(vehiculeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationVehiculeDTO> update(@PathVariable int id,
                                                         @Valid @RequestBody ModifierReservationVehiculeDTO request) {
        return ResponseEntity.ok(reservationVehiculeService.update(id, request));
    }

    @PatchMapping("/{id}/annuler")
    public ResponseEntity<Void> annuler(@PathVariable int id) {
        reservationVehiculeService.annuler(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ReservationVehiculeDTO> create(@Valid @RequestBody ReserverVehiculeServiceDTO request) {
        return ResponseEntity.status(201).body(reservationVehiculeService.create(request));
    }
}
