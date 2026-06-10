package gestiontransports.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * Entité stockant les identifiants JWT (JTI) révoqués lors d'une déconnexion.
 * Permet à la couche de sécurité de rejeter les tokens encore valides côté signature
 * mais explicitement invalidés par l'utilisateur, jusqu'à leur date d'expiration.
 */
@Entity
@Table(name = "token_blacklist")
public class TokenBlacklist {

    @Id
    private String jti;

    private LocalDateTime expireAt;

    public TokenBlacklist() {}

    /**
     * Crée une entrée de blacklist pour un token JWT spécifique.
     *
     * @param jti      l'identifiant unique (JWT ID) du token à révoquer
     * @param expireAt la date et heure à partir de laquelle le token aurait expiré naturellement,
     *                 utilisée pour nettoyer automatiquement les entrées obsolètes
     */
    public TokenBlacklist(String jti, LocalDateTime expireAt) {
        this.jti = jti;
        this.expireAt = expireAt;
    }

    public String getJti() { return jti; }
    public LocalDateTime getExpireAt() { return expireAt; }
}
