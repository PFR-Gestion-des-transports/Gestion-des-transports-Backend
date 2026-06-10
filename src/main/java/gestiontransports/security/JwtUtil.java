package gestiontransports.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Composant utilitaire responsable de la génération, de la validation et de l'extraction
 * des informations contenues dans les tokens JWT.
 * La clé de signature et la durée de validité sont configurées via les propriétés applicatives.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Génère un token JWT signé pour l'utilisateur identifié par son adresse email.
     *
     * @param email l'adresse email de l'utilisateur, utilisée comme sujet du token
     * @param roles la liste des rôles de l'utilisateur, incluse comme claim personnalisée
     * @return le token JWT compact et signé
     */
    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(email)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrait l'adresse email (sujet) encodée dans le token JWT.
     *
     * @param token le token JWT à analyser
     * @return l'adresse email contenue dans le claim {@code sub}
     */
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extrait l'identifiant unique ({@code jti}) du token JWT, utilisé notamment
     * pour la gestion de la liste noire lors de la déconnexion.
     *
     * @param token le token JWT à analyser
     * @return la valeur du claim {@code jti}
     */
    public String extractJti(String token) {
        return getClaims(token).getId();
    }

    /**
     * Extrait la date et l'heure d'expiration du token JWT, converties en
     * {@link LocalDateTime} selon le fuseau horaire système.
     *
     * @param token le token JWT à analyser
     * @return la date et l'heure d'expiration du token
     */
    public LocalDateTime extractExpiration(String token) {
        return getClaims(token).getExpiration()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * Vérifie que le token JWT est valide : le sujet correspond à l'email fourni
     * et le token n'est pas expiré. Retourne {@code false} en cas d'exception de parsing.
     *
     * @param token le token JWT à valider
     * @param email l'adresse email attendue comme sujet du token
     * @return {@code true} si le token est valide et non expiré, {@code false} sinon
     */
    public boolean isTokenValid(String token, String email) {
        try {
            return extractEmail(token).equals(email) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
