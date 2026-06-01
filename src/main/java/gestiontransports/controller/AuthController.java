package gestiontransports.controller;

import gestiontransports.dto.ConnexionRequest;
import gestiontransports.dto.ConnexionResponse;
import gestiontransports.dto.CreerCompteRequest;
import gestiontransports.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/creer-compte")
    public ResponseEntity<ConnexionResponse> creerCompte(@Valid @RequestBody CreerCompteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.creerCompte(request));
    }

    @PostMapping("/se-connecter")
    public ResponseEntity<ConnexionResponse> seConnecter(@Valid @RequestBody ConnexionRequest request) {
        return ResponseEntity.ok(authService.seConnecter(request));
    }

    @PostMapping("/se-deconnecter")
    public ResponseEntity<Map<String, String>> seDeconnecter() {
        return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
    }
}
