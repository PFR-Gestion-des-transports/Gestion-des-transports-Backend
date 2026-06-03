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

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/creer-compte")
    public ResponseEntity<ConnexionResponseDTO> creerCompte(@Valid @RequestBody CreerCompteRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.creerCompte(request));
    }

    @PostMapping("/se-connecter")
    public ResponseEntity<ConnexionResponseDTO> seConnecter(@Valid @RequestBody ConnexionRequestDTO request) {
        return ResponseEntity.ok(authService.seConnecter(request));
    }

    @PostMapping("/se-deconnecter")
    public ResponseEntity<Map<String, String>> seDeconnecter(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        authService.seDeconnecter(token);
        return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
    }
}
