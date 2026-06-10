package gestiontransports.service;

import gestiontransports.dto.reservation.AnnulerReservationCovoiturageDTO;
import gestiontransports.dto.reservation.CreerReservationCovoiturageDTO;
import gestiontransports.dto.reservation.ReservationCovoiturageDTO;
import gestiontransports.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import gestiontransports.model.*;
import gestiontransports.enums.StatutReservation;
import gestiontransports.enums.StatutCovoiturage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationCovoiturageServiceTest {

    @Mock
    private ReservationCovoiturageRepository reservationCovoiturageRepository;
    @Mock
    private UtilisateurContextService utilisateurContextService;
    @Mock
    private CovoiturageRepository covoiturageRepository;

    @InjectMocks
    private ReservationCovoiturageService service;

    private Utilisateur mockUtilisateur;
    private Covoiturage mockCovoiturage;
    private ReservationCovoiturage mockReservation;

    @BeforeEach
    void setUp() {
        mockUtilisateur = new Utilisateur(); // Adaptez si Utilisateur possède un constructeur spécifique
        mockUtilisateur.setId(1);
        Adresse utilisateurAdresse = new Adresse();
        utilisateurAdresse.setId(3);
        mockUtilisateur.setAdresse(utilisateurAdresse);
        
        mockCovoiturage = new Covoiturage();
        mockCovoiturage.setId(10);
        mockCovoiturage.setNbrPlaceRestante(3);
        mockCovoiturage.setNbrPlaceInitial(3);
        mockCovoiturage.setStatut(StatutCovoiturage.PAS_COMMENCER); // Statut valide pour réserver
        Vehicule vehicule = new Vehicule();
        vehicule.setId(1);
        mockCovoiturage.setVehicule(vehicule);
        Adresse adresseDepart = new Adresse();
        adresseDepart.setId(1);
        mockCovoiturage.setAdresseDepart(adresseDepart);
        Adresse adresseArrivee = new Adresse();
        adresseArrivee.setId(2);
        mockCovoiturage.setAdresseArrivee(adresseArrivee);

        mockReservation = new ReservationCovoiturage();
        mockReservation.setId(1);
        mockReservation.setCovoiturage(mockCovoiturage);
        mockReservation.setUtilisateur(mockUtilisateur);
        mockReservation.setStatut(StatutReservation.PAS_COMMENCEE);
    }

    @Test
    void findById_ShouldReturnDto_WhenIdExists() {
        when(reservationCovoiturageRepository.findById(1)).thenReturn(Optional.of(mockReservation));

        ReservationCovoiturageDTO result = service.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(reservationCovoiturageRepository, times(1)).findById(1);
    }

    @Test
    void findById_ShouldThrowException_WhenIdDoesNotExist() {
        when(reservationCovoiturageRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.findById(1));
    }

    @Test
    void findAll_ShouldReturnList() {
        when(reservationCovoiturageRepository.findAll()).thenReturn(Arrays.asList(mockReservation));

        List<ReservationCovoiturageDTO> result = service.findAll();

        assertEquals(1, result.size());
        verify(reservationCovoiturageRepository, times(1)).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoReservations() {
        when(reservationCovoiturageRepository.findAll()).thenReturn(Collections.emptyList());

        List<ReservationCovoiturageDTO> result = service.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(reservationCovoiturageRepository, times(1)).findAll();
    }

    @Test
    void create_ShouldSucceed_WhenDataIsValid() {
        CreerReservationCovoiturageDTO request = new CreerReservationCovoiturageDTO();
        request.setCovoiturageId(10);

        when(utilisateurContextService.getCurrentUser()).thenReturn(mockUtilisateur);
        when(covoiturageRepository.findById(10)).thenReturn(Optional.of(mockCovoiturage));
        when(reservationCovoiturageRepository.save(any(ReservationCovoiturage.class))).thenReturn(mockReservation);

        ReservationCovoiturageDTO result = service.create(request);

        assertNotNull(result);
        assertEquals(2, mockCovoiturage.getNbrPlaceRestante()); // 3 - 1 place
        verify(reservationCovoiturageRepository, times(1)).save(any(ReservationCovoiturage.class));
    }

    @Test
    void create_ShouldThrowException_WhenNoPlacesLeft() {
        CreerReservationCovoiturageDTO request = new CreerReservationCovoiturageDTO();
        request.setCovoiturageId(10);
        mockCovoiturage.setNbrPlaceRestante(0); // Plus de places

        when(utilisateurContextService.getCurrentUser()).thenReturn(mockUtilisateur);
        when(covoiturageRepository.findById(10)).thenReturn(Optional.of(mockCovoiturage));

        assertThrows(RuntimeException.class, () -> service.create(request));
        verify(reservationCovoiturageRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowException_WhenCovoiturageNotFound() {
        CreerReservationCovoiturageDTO request = new CreerReservationCovoiturageDTO();
        request.setCovoiturageId(10);

        when(utilisateurContextService.getCurrentUser()).thenReturn(mockUtilisateur);
        when(covoiturageRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.create(request));
        verify(reservationCovoiturageRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowException_WhenCovoiturageIsInProgress() {
        CreerReservationCovoiturageDTO request = new CreerReservationCovoiturageDTO();
        request.setCovoiturageId(10);
        mockCovoiturage.setStatut(StatutCovoiturage.EN_COURS); // Statut invalide

        when(utilisateurContextService.getCurrentUser()).thenReturn(mockUtilisateur);
        when(covoiturageRepository.findById(10)).thenReturn(Optional.of(mockCovoiturage));

        assertThrows(RuntimeException.class, () -> service.create(request));
    }

    @Test
    void create_ShouldThrowException_WhenCovoiturageIsTermine() {
        CreerReservationCovoiturageDTO request = new CreerReservationCovoiturageDTO();
        request.setCovoiturageId(10);
        mockCovoiturage.setStatut(StatutCovoiturage.TERMINE);

        when(utilisateurContextService.getCurrentUser()).thenReturn(mockUtilisateur);
        when(covoiturageRepository.findById(10)).thenReturn(Optional.of(mockCovoiturage));

        assertThrows(RuntimeException.class, () -> service.create(request));
        verify(reservationCovoiturageRepository, never()).save(any());
    }

    @Test
    void annuler_ShouldSucceed() {
        AnnulerReservationCovoiturageDTO request = new AnnulerReservationCovoiturageDTO();
        request.setReservationId(1);

        when(reservationCovoiturageRepository.findById(1)).thenReturn(Optional.of(mockReservation));
        when(reservationCovoiturageRepository.save(any(ReservationCovoiturage.class))).thenReturn(mockReservation);

        int initialPlaces = mockCovoiturage.getNbrPlaceRestante(); // 3

        ReservationCovoiturageDTO result = service.annuler(request);

        assertEquals(initialPlaces + 1, mockCovoiturage.getNbrPlaceRestante()); // Devrait être 4
        verify(reservationCovoiturageRepository, times(1)).save(mockReservation);
    }

    @Test
    void annuler_ShouldThrowException_WhenReservationNotFound() {
        AnnulerReservationCovoiturageDTO request = new AnnulerReservationCovoiturageDTO();
        request.setReservationId(1);

        when(reservationCovoiturageRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.annuler(request));
        verify(reservationCovoiturageRepository, never()).save(any());
    }

    @Test
    void delete_ShouldSucceed_WhenIdExists() {
        when(reservationCovoiturageRepository.existsById(1)).thenReturn(true);
        when(reservationCovoiturageRepository.findById(1)).thenReturn(Optional.of(mockReservation));

        int initialPlaces = mockCovoiturage.getNbrPlaceRestante();

        service.delete(1);

        assertEquals(initialPlaces + 1, mockCovoiturage.getNbrPlaceRestante());
        verify(reservationCovoiturageRepository, times(1)).deleteById(1);
    }

    @Test
    void delete_ShouldThrowException_WhenIdDoesNotExist() {
        when(reservationCovoiturageRepository.existsById(1)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.delete(1));
        verify(reservationCovoiturageRepository, never()).deleteById(anyInt());
    }
}