package gestiontransports.controller;

import gestiontransports.dto.securite.ConnexionRequestDTO;
import gestiontransports.dto.securite.ConnexionResponseDTO;
import gestiontransports.dto.securite.CreerCompteRequestDTO;
import gestiontransports.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Contrôleur REST gérant l'authentification des utilisateurs.
 * Expose les routes d'inscription, de connexion et de déconnexion sous le préfixe {@code /auth}.
 * Aucune authentification préalable n'est requise pour accéder à ces endpoints.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Crée un nouveau compte utilisateur et retourne un token JWT.
     *
     * @param request les informations de création de compte (email, mot de passe, nom, prénom, etc.)
     * @return une réponse HTTP 201 contenant le token JWT et les informations du compte créé
     */
    @PostMapping("/creer-compte")
    public ResponseEntity<ConnexionResponseDTO> creerCompte(@Valid @RequestBody CreerCompteRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.creerCompte(request));
    }

    /**
     * Authentifie un utilisateur existant à partir de ses identifiants et retourne un token JWT.
     *
     * @param request les identifiants de connexion (email et mot de passe)
     * @return une réponse HTTP 200 contenant le token JWT et les informations de l'utilisateur
     */
    @PostMapping("/se-connecter")
    public ResponseEntity<ConnexionResponseDTO> seConnecter(@Valid @RequestBody ConnexionRequestDTO request) {
        return ResponseEntity.ok(authService.seConnecter(request));
    }

    /**
     * Invalide le token JWT de l'utilisateur connecté, mettant fin à sa session.
     *
     * @param authHeader l'en-tête {@code Authorization} contenant le token Bearer à invalider
     * @return une réponse HTTP 200 avec un message de confirmation de déconnexion
     */
    @PostMapping("/se-deconnecter")
    public ResponseEntity<Map<String, String>> seDeconnecter(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        authService.seDeconnecter(token);
        return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
    }
}
