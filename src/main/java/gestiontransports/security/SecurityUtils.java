package gestiontransports.security;

import gestiontransports.model.Utilisateur;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {}

    public static String currentEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRATEUR"));
    }

    public static boolean isUserAuthorizedAdminAndSelf(Utilisateur utilisateur) {
        return isAdmin() || utilisateur.getEmail().equals(currentEmail());
    }
}
