package gestiontransports.service;

import gestiontransports.dto.securite.ConnexionRequestDTO;
import gestiontransports.dto.securite.ConnexionResponseDTO;
import gestiontransports.dto.securite.CreerCompteRequestDTO;
import gestiontransports.enums.Role;
import gestiontransports.model.Adresse;
import gestiontransports.model.Utilisateur;
import gestiontransports.repository.AdresseRepository;
import gestiontransports.repository.UtilisateurRepository;
import gestiontransports.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final AdresseRepository adresseRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final TokenBlacklistService tokenBlacklistService;

    public AuthService(UtilisateurRepository utilisateurRepository,
                       AdresseRepository adresseRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       TokenBlacklistService tokenBlacklistService) {
        this.utilisateurRepository = utilisateurRepository;
        this.adresseRepository = adresseRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Transactional
    public ConnexionResponseDTO creerCompte(CreerCompteRequestDTO request) {
        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email déjà utilisé");
        }

        Adresse adresse = adresseRepository
                .findByVilleAndRueAndNumeroRue(request.getVille(), request.getRue(), request.getNumeroRue())
                .orElseGet(() -> {
                    Adresse nouvelleAdresse = new Adresse();
                    nouvelleAdresse.setVille(request.getVille());
                    nouvelleAdresse.setRue(request.getRue());
                    nouvelleAdresse.setNumeroRue(request.getNumeroRue());
                    return adresseRepository.save(nouvelleAdresse);
                });

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setNom(request.getNom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        utilisateur.setAdresse(adresse);
        utilisateur.setRoles(Set.of(Role.COLLABORATEUR));

        utilisateurRepository.save(utilisateur);

        String token = jwtUtil.generateToken(
                utilisateur.getEmail(),
                utilisateur.getRoles().stream().map(Enum::name).toList()
        );
        return new ConnexionResponseDTO(token);
    }

    public ConnexionResponseDTO seConnecter(ConnexionRequestDTO request) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants invalides"));

        if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants invalides");
        }

        String token = jwtUtil.generateToken(
                utilisateur.getEmail(),
                utilisateur.getRoles().stream().map(Enum::name).toList()
        );
        return new ConnexionResponseDTO(token);
    }

    public void seDeconnecter(String token) {
        tokenBlacklistService.revoquer(token);
    }
}
