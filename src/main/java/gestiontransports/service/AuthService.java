package gestiontransports.service;

import gestiontransports.dto.ConnexionRequest;
import gestiontransports.dto.ConnexionResponse;
import gestiontransports.dto.CreerCompteRequest;
import gestiontransports.model.Adresse;
import gestiontransports.model.Utilisateur;
import gestiontransports.repository.AdresseRepository;
import gestiontransports.repository.UtilisateurRepository;
import gestiontransports.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final AdresseRepository adresseRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UtilisateurRepository utilisateurRepository,
                       AdresseRepository adresseRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.utilisateurRepository = utilisateurRepository;
        this.adresseRepository = adresseRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public ConnexionResponse creerCompte(CreerCompteRequest request) {
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
        utilisateur.setRoles(request.getRoles());

        utilisateurRepository.save(utilisateur);

        String token = jwtUtil.generateToken(
                utilisateur.getEmail(),
                utilisateur.getRoles().stream().map(Enum::name).toList()
        );
        return new ConnexionResponse(token);
    }

    public ConnexionResponse seConnecter(ConnexionRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants invalides"));

        if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants invalides");
        }

        String token = jwtUtil.generateToken(
                utilisateur.getEmail(),
                utilisateur.getRoles().stream().map(Enum::name).toList()
        );
        return new ConnexionResponse(token);
    }
}
