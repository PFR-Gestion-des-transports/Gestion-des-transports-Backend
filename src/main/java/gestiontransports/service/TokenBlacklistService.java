package gestiontransports.service;

import gestiontransports.model.TokenBlacklist;
import gestiontransports.repository.TokenBlacklistRepository;
import gestiontransports.security.JwtUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenBlacklistService {

    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final JwtUtil jwtUtil;

    public TokenBlacklistService(TokenBlacklistRepository tokenBlacklistRepository, JwtUtil jwtUtil) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
        this.jwtUtil = jwtUtil;
    }

    public void revoquer(String token) {
        String jti = jwtUtil.extractJti(token);
        LocalDateTime expireAt = jwtUtil.extractExpiration(token);
        tokenBlacklistRepository.save(new TokenBlacklist(jti, expireAt));
    }

    public boolean estRevoque(String jti) {
        return tokenBlacklistRepository.existsById(jti);
    }

    @Scheduled(cron = "0 * * * * *")
    public void nettoyerTokensExpires() {
        tokenBlacklistRepository.deleteExpired(LocalDateTime.now());
    }
}
