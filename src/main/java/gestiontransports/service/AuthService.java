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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

/**
 * Service gérant l'authentification des utilisateurs : création de compte, connexion et déconnexion.
 * Les tokens JWT émis lors de la déconnexion sont révoqués via le {@link TokenBlacklistService}.
 */
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

    /**
     * Crée un nouveau compte collaborateur à partir des informations fournies.
     * Si l'adresse renseignée n'existe pas encore en base, elle est créée à la volée.
     * Un token JWT est généré et retourné immédiatement après l'inscription.
     *
     * @param request les données nécessaires à la création du compte (prénom, nom, email, mot de passe, adresse)
     * @return un DTO contenant le token JWT de l'utilisateur nouvellement créé
     * @throws org.springframework.web.server.ResponseStatusException 409 si l'email est déjà associé à un compte existant
     */
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

        try {
            utilisateurRepository.save(utilisateur);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email déjà utilisé");
        }

        String token = jwtUtil.generateToken(
                utilisateur.getEmail(),
                utilisateur.getRoles().stream().map(Enum::name).toList()
        );
        return new ConnexionResponseDTO(token);
    }

    /**
     * Authentifie un utilisateur en vérifiant ses identifiants, puis génère un token JWT en cas de succès.
     *
     * @param request les identifiants de connexion (email et mot de passe)
     * @return un DTO contenant le token JWT valide
     * @throws org.springframework.web.server.ResponseStatusException 401 si l'email est inconnu ou si le mot de passe est incorrect
     */
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

    /**
     * Déconnecte l'utilisateur en révoquant son token JWT courant.
     * Après cet appel, le token est inscrit en liste noire et rejeté pour toute requête ultérieure.
     *
     * @param token le token JWT à invalider
     */
    public void seDeconnecter(String token) {
        tokenBlacklistService.revoquer(token);
    }
}
