package gestiontransports.security;

import gestiontransports.model.Utilisateur;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

/**
 * Classe utilitaire statique exposant des méthodes d'accès au contexte de sécurité courant.
 * Permet de récupérer l'email de l'utilisateur authentifié, de vérifier son rôle administrateur
 * ou de contrôler qu'il agit sur sa propre ressource.
 */
public class SecurityUtils {

    private SecurityUtils() {}

    /**
     * Récupère l'objet {@link Authentication} du contexte de sécurité courant.
     *
     * @return l'authentification active
     * @throws ResponseStatusException avec le statut 401 si aucune authentification n'est présente
     */
    private static Authentication getAuthenticationOrThrow() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non authentifié");
        }
        return auth;
    }

    /**
     * Retourne l'adresse email de l'utilisateur actuellement authentifié.
     *
     * @return l'email extrait du principal de sécurité
     * @throws ResponseStatusException avec le statut 401 si aucune authentification n'est présente
     */
    public static String currentEmail() {
        return getAuthenticationOrThrow().getName();
    }

    /**
     * Indique si l'utilisateur courant possède le rôle {@code ROLE_ADMINISTRATEUR}.
     *
     * @return {@code true} si l'utilisateur est administrateur, {@code false} sinon
     * @throws ResponseStatusException avec le statut 401 si aucune authentification n'est présente
     */
    public static boolean isAdmin() {
        return getAuthenticationOrThrow().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRATEUR"));
    }

    /**
     * Vérifie que l'utilisateur courant est autorisé à agir sur la ressource fournie,
     * c'est-à-dire qu'il est administrateur ou qu'il s'agit de sa propre ressource.
     *
     * @param utilisateur l'entité utilisateur cible de l'opération
     * @return {@code true} si l'utilisateur est administrateur ou s'il correspond à la ressource ciblée
     * @throws ResponseStatusException avec le statut 401 si aucune authentification n'est présente
     */
    public static boolean isUserAuthorizedAdminAndSelf(Utilisateur utilisateur) {
        return isAdmin() || utilisateur.getEmail().equals(currentEmail());
    }
}
