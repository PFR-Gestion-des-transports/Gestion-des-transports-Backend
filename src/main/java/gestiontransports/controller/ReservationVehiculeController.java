package gestiontransports.controller;

import gestiontransports.dto.reservationvehicule.ModifierReservationVehiculeDTO;
import gestiontransports.dto.reservationvehicule.ReservationVehiculeDTO;
import gestiontransports.dto.reservationvehicule.ReserverVehiculeServiceDTO;
import gestiontransports.service.ReservationVehiculeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST exposant les opérations de gestion des réservations de véhicules de service.
 * Préfixe de toutes les routes : {@code /reservations-vehicules}.
 */
@RestController
@RequestMapping("/reservations-vehicules")
public class ReservationVehiculeController {

    private final ReservationVehiculeService reservationVehiculeService;

    public ReservationVehiculeController(ReservationVehiculeService reservationVehiculeService) {
        this.reservationVehiculeService = reservationVehiculeService;
    }

    /**
     * Retourne les réservations actives (non terminées, non annulées) pour un véhicule donné.
     *
     * @param vehiculeId l'identifiant du véhicule
     * @return la liste des réservations actives du véhicule
     */
    @GetMapping("/vehicule/{vehiculeId}")
    public ResponseEntity<List<ReservationVehiculeDTO>> findActivesByVehiculeId(@PathVariable int vehiculeId) {
        return ResponseEntity.ok(reservationVehiculeService.findActivesByVehiculeId(vehiculeId));
    }

    /**
     * Met à jour les dates d'une réservation existante.
     *
     * @param id      l'identifiant de la réservation à modifier
     * @param request le DTO contenant les nouvelles dates de début et de fin
     * @return la réservation mise à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReservationVehiculeDTO> update(@PathVariable int id,
                                                         @Valid @RequestBody ModifierReservationVehiculeDTO request) {
        return ResponseEntity.ok(reservationVehiculeService.update(id, request));
    }

    /**
     * Annule une réservation identifiée par son identifiant.
     *
     * @param id l'identifiant de la réservation à annuler
     * @return une réponse HTTP 204 sans contenu
     */
    @PatchMapping("/{id}/annuler")
    public ResponseEntity<Void> annuler(@PathVariable int id) {
        reservationVehiculeService.annuler(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Crée une nouvelle réservation de véhicule de service.
     *
     * @param request le DTO contenant les informations de la réservation à créer
     * @return la réservation créée avec le statut HTTP 201
     */
    @PostMapping
    public ResponseEntity<ReservationVehiculeDTO> create(@Valid @RequestBody ReserverVehiculeServiceDTO request) {
        return ResponseEntity.status(201).body(reservationVehiculeService.create(request));
    }
}
