package gestiontransports.controller;

import gestiontransports.dto.covoiturage.CovoiturageDTO;
import gestiontransports.dto.covoiturage.CreerCovoiturageRequest;
import gestiontransports.dto.covoiturage.ModifierCovoiturageDTO;
import gestiontransports.service.CovoiturageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/covoiturages")
public class CovoiturageController {

    private final CovoiturageService covoiturageService;

    public CovoiturageController(CovoiturageService covoiturageService) {
        this.covoiturageService = covoiturageService;
    }

    @GetMapping
    public ResponseEntity<List<CovoiturageDTO>> findAll() {
        return ResponseEntity.ok(covoiturageService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CovoiturageDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(covoiturageService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CovoiturageDTO> create(@Valid @RequestBody CreerCovoiturageRequest request) {
        return ResponseEntity.status(201).body(covoiturageService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CovoiturageDTO> update(@PathVariable Integer id,
                                                  @Valid @RequestBody ModifierCovoiturageDTO request) {
        return ResponseEntity.ok(covoiturageService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        covoiturageService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
