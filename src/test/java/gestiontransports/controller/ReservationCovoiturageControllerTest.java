package gestiontransports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gestiontransports.service.ReservationCovoiturageService;
import gestiontransports.dto.reservation.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ReservationCovoiturageController.class)
class ReservationCovoiturageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private gestiontransports.security.JwtUtil jwtUtil;

    @MockBean
    private gestiontransports.security.JwtFilter jwtFilter;

    @MockBean
    private ReservationCovoiturageService service;

    private ReservationCovoiturageDTO mockDto;

    @BeforeEach
    void setUp() {
        mockDto = new ReservationCovoiturageDTO();
        mockDto.setId(1);
        // Vous pouvez initialiser CovoiturageDTO et UtilisateurDTO si nécessaire
    }

    @Test
    void findAll_ShouldReturnOkAndList() throws Exception {
        when(service.findAll()).thenReturn(Arrays.asList(mockDto));

        mockMvc.perform(get("/reservations-covoiturage/findall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void findById_ShouldReturnOkAndDto() throws Exception {
        when(service.findById(1)).thenReturn(mockDto);

        mockMvc.perform(get("/reservations-covoiturage/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_ShouldReturnCreatedAndDto() throws Exception {
        CreerReservationCovoiturageDTO request = new CreerReservationCovoiturageDTO();
        request.setCovoiturageId(10);

        when(service.create(any(CreerReservationCovoiturageDTO.class))).thenReturn(mockDto);

        mockMvc.perform(post("/reservations-covoiturage/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void annuler_ShouldReturnOkAndDto() throws Exception {
        AnnulerReservationCovoiturageDTO request = new AnnulerReservationCovoiturageDTO();
        request.setReservationId(1);

        when(service.annuler(any(AnnulerReservationCovoiturageDTO.class))).thenReturn(mockDto);

        mockMvc.perform(put("/reservations-covoiturage/annuler")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(service).delete(anyInt());

        mockMvc.perform(delete("/reservations-covoiturage/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void findAll_ShouldReturnOkAndEmptyList() throws Exception {
        when(service.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/reservations-covoiturage/findall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void findById_ShouldReturnInternalServerError_WhenNotFound() throws Exception {
        when(service.findById(999)).thenThrow(new RuntimeException("Réservation non trouvée"));

        mockMvc.perform(get("/reservations-covoiturage/999"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void create_ShouldReturnInternalServerError_WhenCreateFails() throws Exception {
        CreerReservationCovoiturageDTO request = new CreerReservationCovoiturageDTO();
        request.setCovoiturageId(10);

        when(service.create(any(CreerReservationCovoiturageDTO.class))).thenThrow(new RuntimeException("Impossible de créer"));

        mockMvc.perform(post("/reservations-covoiturage/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void annuler_ShouldReturnInternalServerError_WhenAnnulerFails() throws Exception {
        AnnulerReservationCovoiturageDTO request = new AnnulerReservationCovoiturageDTO();
        request.setReservationId(999);

        when(service.annuler(any(AnnulerReservationCovoiturageDTO.class))).thenThrow(new RuntimeException("Impossible d'annuler"));

        mockMvc.perform(put("/reservations-covoiturage/annuler")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void delete_ShouldReturnInternalServerError_WhenDeleteFails() throws Exception {
        doThrow(new RuntimeException("Impossible de supprimer")).when(service).delete(anyInt());

        mockMvc.perform(delete("/reservations-covoiturage/1"))
                .andExpect(status().is5xxServerError());
    }
}
