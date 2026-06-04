package gestiontransports.repository;

import gestiontransports.model.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Repository JPA pour l'entité {@link TokenBlacklist}.
 * Gère la persistance des tokens JWT révoqués et leur nettoyage périodique
 * pour éviter une accumulation indéfinie en base de données.
 */
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, String> {

    /**
     * Supprime en base tous les tokens dont la date d'expiration est antérieure à la date fournie.
     * Destinée à être appelée par un scheduler périodique pour purger les entrées obsolètes.
     *
     * @param now la date et heure de référence ; tous les tokens expirés avant cette valeur sont supprimés
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM TokenBlacklist t WHERE t.expireAt < :now")
    void deleteExpired(LocalDateTime now);
}
