package gestiontransports.service;

import gestiontransports.model.TokenBlacklist;
import gestiontransports.repository.TokenBlacklistRepository;
import gestiontransports.security.JwtUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service gérant la liste noire des tokens JWT révoqués lors de la déconnexion.
 * Une tâche planifiée supprime automatiquement les entrées expirées toutes les minutes
 * afin de limiter la croissance de la table en base de données.
 */
@Service
public class TokenBlacklistService {

    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final JwtUtil jwtUtil;

    public TokenBlacklistService(TokenBlacklistRepository tokenBlacklistRepository, JwtUtil jwtUtil) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Révoque un token JWT en extrayant son identifiant unique (JTI) et sa date d'expiration,
     * puis en persistant l'entrée correspondante dans la liste noire.
     *
     * @param token le token JWT à révoquer, tel que reçu dans l'en-tête Authorization
     */
    public void revoquer(String token) {
        String jti = jwtUtil.extractJti(token);
        LocalDateTime expireAt = jwtUtil.extractExpiration(token);
        tokenBlacklistRepository.save(new TokenBlacklist(jti, expireAt));
    }

    /**
     * Indique si un token identifié par son JTI a été révoqué et figure dans la liste noire.
     *
     * @param jti l'identifiant unique du token JWT (claim {@code jti})
     * @return {@code true} si le token est révoqué, {@code false} sinon
     */
    public boolean estRevoque(String jti) {
        if (jti == null) {
            return false;
        }
        return tokenBlacklistRepository.existsById(jti);
    }

    /**
     * Tâche planifiée exécutée toutes les minutes qui supprime de la liste noire
     * les entrées dont la date d'expiration est passée.
     * Cela évite une accumulation indéfinie de tokens révoqués en base de données.
     */
    @Scheduled(cron = "0 * * * * *")
    public void nettoyerTokensExpires() {
        tokenBlacklistRepository.deleteExpired(LocalDateTime.now());
    }
}
