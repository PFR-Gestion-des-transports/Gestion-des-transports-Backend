package gestiontransports.controller;

import gestiontransports.security.JwtUtil;
import gestiontransports.security.UserDetailsServiceImpl;

import gestiontransports.service.CovoiturageService;
import java.util.Optional;

import gestiontransports.repository.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import gestiontransports.dto.covoiturage.CovoiturageDTO;
import gestiontransports.dto.covoiturage.CreerCovoiturageRequest;
import gestiontransports.dto.covoiturage.ModifierCovoiturageDTO;
import gestiontransports.dto.covoiturage.ModifierPlacesCovoiturageDTO;
import gestiontransports.enums.StatutCovoiturage;
import gestiontransports.model.Covoiturage;
import gestiontransports.dto.adresse.*;
import gestiontransports.service.TokenBlacklistService;
import gestiontransports.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CovoiturageController.class)
@AutoConfigureMockMvc(addFilters = false) // Désactive la sécurité (ex: JWT) pour isoler le test du contrôleur
class CovoiturageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CovoiturageService covoiturageService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private TokenBlacklistService tokenBlacklistService;

    private CovoiturageDTO covoiturageDTO;
    private CovoiturageRepository covoiturageRepository;
    private AdresseRepository adresseRepository;
    private VehiculeRepository vehiculeRepository;

    private Covoiturage covoiturage;
    private Vehicule vehicule;
    private Adresse adresseDepart;




    @BeforeEach
    void setUp() {
        // Initialisation d'un DTO générique pour les retours mockés
        covoiturageDTO = new CovoiturageDTO();
        // Si ton DTO possède des setters, tu peux initialiser un ID ici, ex:
        // covoiturageDTO.setId(1);
    }

    @Test
    void findAll_ShouldReturnListOfCovoituragesAndStatus200() throws Exception {
        // Arrange
        when(covoiturageService.findAll()).thenReturn(List.of(covoiturageDTO));

        // Act & Assert
        mockMvc.perform(get("/covoiturages/findall")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

        verify(covoiturageService, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnCovoiturageAndStatus200() throws Exception {
        // Arrange
        Integer id = 1;
        when(covoiturageService.findById(id)).thenReturn(covoiturageDTO);

        // Act & Assert
        mockMvc.perform(get("/covoiturages/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(covoiturageService, times(1)).findById(id);
    }

    @Test
    void create_ShouldReturnCreatedCovoiturageAndStatus201() throws Exception {
        // Arrange
        CreerCovoiturageRequest request = new CreerCovoiturageRequest();
        
        // 1. Les informations de base
        request.setVehiculeId(1);
        request.setNbrPlaceInitial(3);
        request.setDateHeureDebut(LocalDateTime.now().plusDays(3));
        
        // 2. Création et hydratation de l'objet Adresse de départ
        AdresseInputDTO adresseDepart = new AdresseInputDTO();
        adresseDepart.setNumeroRue("10");
        adresseDepart.setRue("Avenue d'Occitanie");
        adresseDepart.setVille("Montpellier");
        request.setAdresseDepart(adresseDepart); // On passe l'objet complet

        // 3. Création et hydratation de l'objet Adresse d'arrivée
        AdresseInputDTO adresseArrivee = new AdresseInputDTO();
        adresseArrivee.setNumeroRue("5");
        adresseArrivee.setRue("Rue de la République");
        adresseArrivee.setVille("Lyon");
        request.setAdresseArrivee(adresseArrivee); // On passe l'objet complet

        // Simulation du service
        when(covoiturageService.create(any(CreerCovoiturageRequest.class))).thenReturn(covoiturageDTO);

        // Act & Assert
        mockMvc.perform(post("/covoiturages/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print()) // Affiche la requête/réponse dans la console
                .andExpect(status().isCreated());

        verify(covoiturageService, times(1)).create(any(CreerCovoiturageRequest.class));
    }

    @Test
    void update_ShouldReturnUpdatedCovoiturageAndStatus200() throws Exception {
        // Arrange
        Integer id = 1;
        ModifierCovoiturageDTO request = new ModifierCovoiturageDTO();
        
        // --- 1. Champs obligatoires simples ---
        request.setVehiculeId(1);
        request.setNbrPlaceInitial(3);
        request.setDateHeureDebut(LocalDateTime.now().plusDays(5));
        
        // C'EST LUI LE COUPABLE ! Ajoute une valeur valide de ton Enum.
        // Remplace "OUVERT" par un vrai statut défini dans ton Enum StatutCovoiturage
        request.setStatut(StatutCovoiturage.EN_COURS); 

        // --- 2. Objet Adresse de départ (Type : AdresseInputDTO) ---
        AdresseInputDTO adresseDepart = new AdresseInputDTO();
        adresseDepart.setNumeroRue("10");
        adresseDepart.setRue("Avenue d'Occitanie");
        adresseDepart.setVille("Montpellier");
        request.setAdresseDepart(adresseDepart);

        // --- 3. Objet Adresse d'arrivée (Type : AdresseInputDTO) ---
        AdresseInputDTO adresseArrivee = new AdresseInputDTO();
        adresseArrivee.setNumeroRue("5");
        adresseArrivee.setRue("Rue de la République");
        adresseArrivee.setVille("Lyon");
        request.setAdresseArrivee(adresseArrivee);

        // Simulation du comportement du service
        when(covoiturageService.update(eq(id), any(ModifierCovoiturageDTO.class))).thenReturn(covoiturageDTO);

        // Act & Assert
        mockMvc.perform(put("/covoiturages/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print()) 
                .andExpect(status().isOk()); // Attente du statut 200

        verify(covoiturageService, times(1)).update(eq(id), any(ModifierCovoiturageDTO.class));
    }

    @Test
    void modifierPlaces_ShouldReturnUpdatedCovoiturageAndStatus200() throws Exception {
        // Arrange
        Integer id = 1;
        ModifierPlacesCovoiturageDTO request = new ModifierPlacesCovoiturageDTO();
        
        // --- LA CORRECTION EST ICI ---
        // On renseigne le champ exigé par la validation @NotNull
        request.setNbrPlaceRestante(2); 

        when(covoiturageService.modifierPlaces(eq(id), any(ModifierPlacesCovoiturageDTO.class))).thenReturn(covoiturageDTO);

        // Act & Assert
        mockMvc.perform(patch("/covoiturages/{id}/places", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print()) // On le garde au cas où ton DTO exigerait autre chose
                .andExpect(status().isOk());

        verify(covoiturageService, times(1)).modifierPlaces(eq(id), any(ModifierPlacesCovoiturageDTO.class));
    }

    @Test
    void deleteById_ShouldReturnStatus204() throws Exception {
        // Arrange
        Integer id = 1;
        doNothing().when(covoiturageService).deleteById(id);

        // Act & Assert
        mockMvc.perform(delete("/covoiturages/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Vérifie le statut 204

        verify(covoiturageService, times(1)).deleteById(id);
    }
}