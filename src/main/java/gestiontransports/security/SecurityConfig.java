package gestiontransports.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration centrale de la sécurité Spring Security.
 * Définit la chaîne de filtres HTTP (politique stateless, routes publiques, intégration du filtre JWT),
 * ainsi que les beans {@link org.springframework.security.authentication.AuthenticationManager}
 * et {@link org.springframework.security.crypto.password.PasswordEncoder} utilisés dans l'application.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Construit et configure la chaîne de filtres de sécurité HTTP : désactivation du CSRF,
     * politique de session sans état, règles d'autorisation des routes publiques et protégées,
     * et insertion du filtre JWT avant le filtre d'authentification standard.
     *
     * @param http le builder de configuration HTTP fourni par Spring Security
     * @return la chaîne de filtres construite
     * @throws Exception en cas d'erreur lors de la configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/creer-compte", "/auth/se-connecter",
                    "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Expose le gestionnaire d'authentification de Spring Security en tant que bean,
     * nécessaire notamment pour le traitement des connexions dans le service d'authentification.
     *
     * @param config la configuration d'authentification fournie par Spring
     * @return le gestionnaire d'authentification configuré
     * @throws Exception en cas d'erreur lors de la récupération du gestionnaire
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Fournit un encodeur de mots de passe basé sur l'algorithme BCrypt,
     * utilisé lors de la création de compte et de la vérification des identifiants.
     *
     * @return une instance de {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
