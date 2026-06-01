package gestiontransports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gestiontransports.repository.AdresseRepository;
import gestiontransports.repository.UtilisateurRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String EMAIL_TEST = "authtest_unique@test.com";

    @BeforeAll
    static void setUp(@Autowired UtilisateurRepository utilisateurRepo) {
        utilisateurRepo.findAll().stream()
                .filter(u -> u.getEmail().equals(EMAIL_TEST))
                .forEach(utilisateurRepo::delete);
    }

    @AfterAll
    static void tearDown(@Autowired AdresseRepository adresseRepo,
                         @Autowired UtilisateurRepository utilisateurRepo) {
        utilisateurRepo.findAll().stream()
                .filter(u -> u.getEmail().equals(EMAIL_TEST))
                .forEach(utilisateurRepo::delete);
        adresseRepo.findByVilleAndRueAndNumeroRue("Paris", "Rue de la Paix", "1")
                .ifPresent(adresseRepo::delete);
    }

    @Test
    @Order(1)
    void creerCompte_retourne201AvecToken() throws Exception {
        Map<String, Object> request = Map.of(
                "prenom", "Jean",
                "nom", "Test",
                "email", EMAIL_TEST,
                "motDePasse", "password123",
                "ville", "Paris",
                "rue", "Rue de la Paix",
                "numeroRue", "1",
                "roles", List.of("COLLABORATEUR")
        );

        mockMvc.perform(post("/auth/creer-compte")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @Order(2)
    void creerCompte_emailDejaUtilise_retourne409() throws Exception {
        Map<String, Object> request = Map.of(
                "prenom", "Jean",
                "nom", "Test",
                "email", EMAIL_TEST,
                "motDePasse", "password123",
                "ville", "Paris",
                "rue", "Rue de la Paix",
                "numeroRue", "1",
                "roles", List.of("COLLABORATEUR")
        );

        mockMvc.perform(post("/auth/creer-compte")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(3)
    void seConnecter_retourne200AvecToken() throws Exception {
        Map<String, String> request = Map.of(
                "email", EMAIL_TEST,
                "motDePasse", "password123"
        );

        mockMvc.perform(post("/auth/se-connecter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @Order(4)
    void seConnecter_mauvaisMotDePasse_retourne401() throws Exception {
        Map<String, String> request = Map.of(
                "email", EMAIL_TEST,
                "motDePasse", "mauvais"
        );

        mockMvc.perform(post("/auth/se-connecter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
