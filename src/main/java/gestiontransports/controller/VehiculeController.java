package gestiontransports.controller;

import gestiontransports.dto.vehicule.CreerVehiculeRequestDTO;
import gestiontransports.dto.vehicule.ModifierStatutVehiculeRequestDTO;
import gestiontransports.dto.vehicule.ModifierVehiculeRequestDTO;
import gestiontransports.dto.vehicule.VehiculeDTO;
import gestiontransports.service.VehiculeService;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST gérant les véhicules de l'application, qu'ils soient personnels ou de service.
 * La création d'un véhicule de service est réservée aux administrateurs ; les autres opérations
 * sont accessibles à tout utilisateur authentifié. Les routes sont exposées sous le préfixe {@code /vehicules}.
 */
@RestController
@RequestMapping("/vehicules")
public class VehiculeController {

    private final VehiculeService vehiculeService;

    public VehiculeController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @GetMapping("/service/mes-reservations")
    public ResponseEntity<List<VehiculeDTO>> findVehiculesServiceReservesParUtilisateur(
            @RequestParam LocalDateTime dateDebut,
            @RequestParam LocalDateTime dateFin) {
        return ResponseEntity.ok(vehiculeService.findVehiculesServiceReservesParUtilisateur(dateDebut, dateFin));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<VehiculeDTO>> findVehiculesDisponibles() {
        return ResponseEntity.ok(vehiculeService.findVehiculesDisponibles());
    }

    /**
     * Récupère le détail d'un véhicule à partir de son identifiant.
     *
     * @param id l'identifiant du véhicule recherché
     * @return une réponse HTTP 200 contenant les informations du véhicule
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDTO> findById(@PathVariable int id) {
        return ResponseEntity.ok(vehiculeService.findById(id));
    }

    /**
     * Crée un nouveau véhicule personnel associé à l'utilisateur connecté.
     *
     * @param request les données du véhicule à créer (marque, modèle, immatriculation, etc.)
     * @return une réponse HTTP 201 contenant le véhicule nouvellement créé
     */
    @PostMapping
    public ResponseEntity<VehiculeDTO> create(@Valid @RequestBody CreerVehiculeRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeService.create(request, false));
    }

    /**
     * Crée un nouveau véhicule de service de l'entreprise.
     * Accessible uniquement aux administrateurs.
     *
     * @param request les données du véhicule de service à créer
     * @return une réponse HTTP 201 contenant le véhicule de service nouvellement créé
     */
    @PostMapping("/service")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<VehiculeDTO> createService(@Valid @RequestBody CreerVehiculeRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeService.create(request, true));
    }

    /**
     * Met à jour intégralement les informations d'un véhicule existant.
     *
     * @param id      l'identifiant du véhicule à modifier
     * @param request les nouvelles données du véhicule
     * @return une réponse HTTP 200 contenant le véhicule mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehiculeDTO> update(@PathVariable int id,
                                               @Valid @RequestBody ModifierVehiculeRequestDTO request) {
        return ResponseEntity.ok(vehiculeService.update(id, request));
    }

    /**
     * Modifie uniquement le statut d'un véhicule (disponible, en maintenance, etc.).
     *
     * @param id      l'identifiant du véhicule dont le statut doit être modifié
     * @param request le nouveau statut à appliquer
     * @return une réponse HTTP 200 contenant le véhicule avec son statut mis à jour
     */
    @PatchMapping("/{id}/statut")
    public ResponseEntity<VehiculeDTO> modifierStatut(@PathVariable int id,
                                                       @Valid @RequestBody ModifierStatutVehiculeRequestDTO request) {
        return ResponseEntity.ok(vehiculeService.modifierStatut(id, request));
    }

    /**
     * Supprime définitivement un véhicule à partir de son identifiant.
     *
     * @param id l'identifiant du véhicule à supprimer
     * @return une réponse HTTP 204 sans contenu
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        vehiculeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
