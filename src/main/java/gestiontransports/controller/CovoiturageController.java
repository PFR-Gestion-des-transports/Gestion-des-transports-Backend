package gestiontransports.controller;

import gestiontransports.dto.covoiturage.CovoiturageDTO;
import gestiontransports.dto.covoiturage.CreerCovoiturageRequest;
import gestiontransports.dto.covoiturage.ModifierCovoiturageDTO;
import gestiontransports.service.CovoiturageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST exposant les opérations CRUD sur les covoiturages d'entreprise.
 * Toutes les routes sont protégées par authentification JWT et accessibles sous le préfixe {@code /covoiturages}.
 */
@RestController
@RequestMapping("/covoiturages")
public class CovoiturageController {

    private final CovoiturageService covoiturageService;

    public CovoiturageController(CovoiturageService covoiturageService) {
        this.covoiturageService = covoiturageService;
    }

    /**
     * Récupère la liste de tous les covoiturages enregistrés.
     *
     * @return une réponse HTTP 200 contenant la liste des covoiturages
     */
    @GetMapping
    public ResponseEntity<List<CovoiturageDTO>> findAll() {
        return ResponseEntity.ok(covoiturageService.findAll());
    }

    /**
     * Récupère le détail d'un covoiturage à partir de son identifiant.
     *
     * @param id l'identifiant du covoiturage recherché
     * @return une réponse HTTP 200 contenant le covoiturage correspondant
     */
    @GetMapping("/{id}")
    public ResponseEntity<CovoiturageDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(covoiturageService.findById(id));
    }

    /**
     * Crée un nouveau covoiturage.
     *
     * @param request les données du covoiturage à créer (départ, arrivée, date, places, etc.)
     * @return une réponse HTTP 201 contenant le covoiturage nouvellement créé
     */
    @PostMapping
    public ResponseEntity<CovoiturageDTO> create(@Valid @RequestBody CreerCovoiturageRequest request) {
        return ResponseEntity.status(201).body(covoiturageService.create(request));
    }

    /**
     * Met à jour intégralement un covoiturage existant.
     *
     * @param id      l'identifiant du covoiturage à modifier
     * @param request les nouvelles données du covoiturage
     * @return une réponse HTTP 200 contenant le covoiturage mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<CovoiturageDTO> update(@PathVariable Integer id,
                                                  @Valid @RequestBody ModifierCovoiturageDTO request) {
        return ResponseEntity.ok(covoiturageService.update(id, request));
    }

    /**
     * Supprime définitivement un covoiturage à partir de son identifiant.
     *
     * @param id l'identifiant du covoiturage à supprimer
     * @return une réponse HTTP 204 sans contenu
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        covoiturageService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
