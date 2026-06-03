package gestiontransports.security;

import gestiontransports.model.Utilisateur;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public class SecurityUtils {

    private SecurityUtils() {}

    private static Authentication getAuthenticationOrThrow() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non authentifié");
        }
        return auth;
    }

    public static String currentEmail() {
        return getAuthenticationOrThrow().getName();
    }

    public static boolean isAdmin() {
        return getAuthenticationOrThrow().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRATEUR"));
    }

    public static boolean isUserAuthorizedAdminAndSelf(Utilisateur utilisateur) {
        return isAdmin() || utilisateur.getEmail().equals(currentEmail());
    }
}
