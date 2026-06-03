package gestiontransports.service;

import gestiontransports.model.Utilisateur;
import gestiontransports.repository.UtilisateurRepository;
import gestiontransports.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service utilitaire résolvant l'utilisateur actuellement authentifié en entité JPA.
 * Centralise le pattern {@code findByEmail(SecurityUtils.currentEmail()).orElseThrow()}
 * utilisé dans plusieurs services métier.
 */
@Service
public class UtilisateurContextService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurContextService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Retourne l'entité {@link Utilisateur} correspondant à l'utilisateur authentifié dans le contexte courant.
     *
     * @return l'utilisateur courant
     * @throws ResponseStatusException 404 si l'email du token ne correspond à aucun compte en base
     */
    public Utilisateur getCurrentUser() {
        return utilisateurRepository.findByEmail(SecurityUtils.currentEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
    }
}
