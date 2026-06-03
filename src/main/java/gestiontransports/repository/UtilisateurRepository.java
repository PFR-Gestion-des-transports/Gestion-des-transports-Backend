package gestiontransports.repository;

import gestiontransports.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository JPA pour l'entité {@link Utilisateur}.
 * Étend les opérations CRUD standard avec une recherche par email,
 * utilisée notamment lors de l'authentification JWT.
 */
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {

    /**
     * Recherche un utilisateur par son adresse email.
     * Utilisée par la couche de sécurité pour charger les détails de l'utilisateur lors de la
     * vérification du token JWT ou lors de la connexion.
     *
     * @param email l'adresse email unique de l'utilisateur
     * @return un {@link Optional} contenant l'utilisateur si trouvé, vide sinon
     */
    Optional<Utilisateur> findByEmail(String email);
}
