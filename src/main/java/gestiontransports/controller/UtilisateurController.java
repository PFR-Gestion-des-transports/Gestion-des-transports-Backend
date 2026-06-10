package gestiontransports.controller;

import gestiontransports.dto.utilisateur.ModifierUtilisateurRequestDTO;
import gestiontransports.dto.utilisateur.UtilisateurDTO;
import gestiontransports.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST gérant les opérations CRUD sur les utilisateurs de l'application.
 * Certaines routes sont restreintes au rôle ADMINISTRATEUR ; un utilisateur peut également
 * consulter et modifier son propre profil. Les routes sont exposées sous le préfixe {@code /utilisateurs}.
 */
@RestController
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    /**
     * Récupère la liste de tous les utilisateurs enregistrés.
     * Accessible uniquement aux administrateurs.
     *
     * @return une réponse HTTP 200 contenant la liste complète des utilisateurs
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<List<UtilisateurDTO>> findAll() {
        return ResponseEntity.ok(utilisateurService.findAll());
    }

    /**
     * Récupère le profil d'un utilisateur à partir de son identifiant.
     *
     * @param id l'identifiant de l'utilisateur recherché
     * @return une réponse HTTP 200 contenant le profil de l'utilisateur
     */
    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> findById(@PathVariable int id) {
        return ResponseEntity.ok(utilisateurService.findById(id));
    }

    /**
     * Met à jour les informations d'un utilisateur existant.
     * Un collaborateur ne peut modifier que son propre profil ; un administrateur peut modifier n'importe quel profil.
     *
     * @param id      l'identifiant de l'utilisateur à modifier
     * @param request les nouvelles données de l'utilisateur
     * @return une réponse HTTP 200 contenant le profil mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> update(@PathVariable int id,
                                                  @Valid @RequestBody ModifierUtilisateurRequestDTO request) {
        return ResponseEntity.ok(utilisateurService.update(id, request));
    }

    /**
     * Supprime définitivement un utilisateur à partir de son identifiant.
     * Accessible uniquement aux administrateurs.
     *
     * @param id l'identifiant de l'utilisateur à supprimer
     * @return une réponse HTTP 204 sans contenu
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        utilisateurService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
