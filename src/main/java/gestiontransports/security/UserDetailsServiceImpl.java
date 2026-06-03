package gestiontransports.security;

import gestiontransports.model.Utilisateur;
import gestiontransports.repository.UtilisateurRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation de {@link UserDetailsService} qui charge les informations d'un utilisateur
 * depuis la base de données à partir de son adresse email, puis construit l'objet
 * {@link UserDetails} avec ses rôles préfixés par {@code ROLE_} pour Spring Security.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    public UserDetailsServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Charge un utilisateur depuis la base de données à partir de son adresse email
     * et construit l'objet {@link UserDetails} correspondant avec ses autorités.
     *
     * @param email l'adresse email servant d'identifiant de connexion
     * @return les détails de l'utilisateur prêts à être utilisés par Spring Security
     * @throws UsernameNotFoundException si aucun utilisateur ne correspond à l'email fourni
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));

        List<SimpleGrantedAuthority> authorities = utilisateur.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();

        return new User(utilisateur.getEmail(), utilisateur.getMotDePasse(), authorities);
    }
}
