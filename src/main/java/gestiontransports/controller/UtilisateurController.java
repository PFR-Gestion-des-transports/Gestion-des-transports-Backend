package gestiontransports.controller;

import gestiontransports.dto.utilisateur.ModifierUtilisateurRequestDTO;
import gestiontransports.dto.utilisateur.UtilisateurDTO;
import gestiontransports.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<List<UtilisateurDTO>> findAll() {
        return ResponseEntity.ok(utilisateurService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(utilisateurService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> update(@PathVariable Integer id,
                                                  @Valid @RequestBody ModifierUtilisateurRequestDTO request) {
        return ResponseEntity.ok(utilisateurService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        utilisateurService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
