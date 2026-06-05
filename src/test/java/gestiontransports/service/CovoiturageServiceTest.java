package gestiontransports.service;

import gestiontransports.dto.covoiturage.CovoiturageDTO;
import gestiontransports.dto.covoiturage.CreerCovoiturageRequest;
import gestiontransports.dto.covoiturage.ModifierCovoiturageDTO;
import gestiontransports.dto.covoiturage.ModifierPlacesCovoiturageDTO;
import gestiontransports.dto.adresse.*; // Assumé selon le contexte précédent
import gestiontransports.enums.StatutCovoiturage;
import gestiontransports.model.Adresse;
import gestiontransports.model.Covoiturage;
import gestiontransports.model.Utilisateur;
import gestiontransports.model.Vehicule;
import gestiontransports.repository.AdresseRepository;
import gestiontransports.repository.CovoiturageRepository;
import gestiontransports.repository.VehiculeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CovoiturageServiceTest {

    @Mock
    private CovoiturageRepository covoiturageRepository;

    @Mock
    private AdresseRepository adresseRepository;

    @Mock
    private VehiculeRepository vehiculeRepository;

    @Mock
    private UtilisateurContextService utilisateurContextService;

    @InjectMocks
    private CovoiturageService covoiturageService;

    private Utilisateur utilisateur;
    private Vehicule vehicule;
    private Covoiturage covoiturage;
    private Adresse adresseDepart;

    @BeforeEach
    void setUp() {
        // Initialisation de données communes pour éviter la répétition dans chaque test
        utilisateur = new Utilisateur();
        utilisateur.setId(1);

        vehicule = new Vehicule();
        vehicule.setId(1);
        vehicule.setNombreDePlace(4); // Capacité du véhicule fixée à 4

        adresseDepart = new Adresse();
        adresseDepart.setId(1);
        adresseDepart.setVille("Montpellier");
        adresseDepart.setRue("Avenue d'Occitanie");
        adresseDepart.setNumeroRue("10");

        covoiturage = new Covoiturage();
        covoiturage.setId(1);
        covoiturage.setNbrPlaceInitial(3);
        covoiturage.setNbrPlaceRestante(3);
        covoiturage.setStatut(StatutCovoiturage.PAS_COMMENCER);
        covoiturage.setVehicule(vehicule);
        covoiturage.setUtilisateur(utilisateur);
        covoiturage.setAdresseDepart(adresseDepart);
        covoiturage.setAdresseArrivee(adresseDepart); // Simplification pour le test
        // On met une date dans le futur (> 1 jour) pour passer la vérification isBeetwenOneDay
        covoiturage.setDateHeureDebut(LocalDateTime.now().plusDays(5)); 
    }

    // ==========================================
    // TESTS FIND ALL & FIND BY ID
    // ==========================================

    @Test
    void findAll_ShouldReturnList() {
        when(covoiturageRepository.findAll()).thenReturn(List.of(covoiturage));

        List<CovoiturageDTO> result = covoiturageService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(covoiturageRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnCovoiturage_WhenExists() {
        when(covoiturageRepository.findById(1)).thenReturn(Optional.of(covoiturage));

        CovoiturageDTO result = covoiturageService.findById(1);

        assertNotNull(result);
        verify(covoiturageRepository, times(1)).findById(1);
    }

    @Test
    void findById_ShouldThrow404_WhenNotExists() {
        when(covoiturageRepository.findById(99)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
                () -> covoiturageService.findById(99));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    // ==========================================
    // TESTS CREATE
    // ==========================================

    @Test
    void create_ShouldSucceed_WhenDataIsValid() {
        // Arrange
        CreerCovoiturageRequest request = createMockCreateRequest(3);
        
        when(utilisateurContextService.getCurrentUser()).thenReturn(utilisateur);
        when(vehiculeRepository.findById(1)).thenReturn(Optional.of(vehicule));
        when(covoiturageRepository.existsByVehiculeAndDatesOverlapping(anyInt(), any(LocalDateTime.class))).thenReturn(false);
        when(adresseRepository.findByVilleAndRueAndNumeroRue(anyString(), anyString(), anyString())).thenReturn(Optional.of(adresseDepart));
        
        // CORRECTION ICI : On intercepte l'objet sauvegardé et on lui donne un ID
        when(covoiturageRepository.save(any(Covoiturage.class))).thenAnswer(invocation -> {
            Covoiturage savedCovoiturage = invocation.getArgument(0);
            savedCovoiturage.setId(1); 
            return savedCovoiturage;
        });

        // Act
        CovoiturageDTO result = covoiturageService.create(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId()); // On peut même vérifier que l'ID a bien été retourné !
        verify(covoiturageRepository, times(1)).save(any(Covoiturage.class));
    }

    @Test
    void create_ShouldThrow400_WhenPlacesExceedVehicleCapacity() {
        // Arrange : On demande 5 places pour un véhicule qui n'en a que 4
        CreerCovoiturageRequest request = createMockCreateRequest(5);
        
        when(utilisateurContextService.getCurrentUser()).thenReturn(utilisateur);
        when(vehiculeRepository.findById(1)).thenReturn(Optional.of(vehicule));
        when(covoiturageRepository.existsByVehiculeAndDatesOverlapping(anyInt(), any(LocalDateTime.class))).thenReturn(false);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
                () -> covoiturageService.create(request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("dépasser la capacité du véhicule"));
        verify(covoiturageRepository, never()).save(any(Covoiturage.class));
    }

    // ==========================================
    // TESTS UPDATE
    // ==========================================

    @Test
    void update_ShouldThrow400_WhenStatusIsTermine() {
        // Arrange
        covoiturage.setStatut(StatutCovoiturage.TERMINE); // Le covoiturage est terminé en base
        ModifierCovoiturageDTO request = new ModifierCovoiturageDTO();
        
        when(covoiturageRepository.findById(1)).thenReturn(Optional.of(covoiturage));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
                () -> covoiturageService.update(1, request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Impossible de modifier un covoiturage terminé", exception.getReason());
    }

    @Test
    void update_ShouldThrow400_WhenLessThanOneDayLeft() {
        // Arrange
        // On modifie la date pour qu'elle soit dans moins de 24h
        covoiturage.setDateHeureDebut(LocalDateTime.now().plusHours(12)); 
        ModifierCovoiturageDTO request = new ModifierCovoiturageDTO();
        
        when(covoiturageRepository.findById(1)).thenReturn(Optional.of(covoiturage));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
                () -> covoiturageService.update(1, request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Impossible de modifier un covoiturage déjà commencé", exception.getReason());
    }

    // ==========================================
    // TESTS MODIFIER PLACES
    // ==========================================

    @Test
    void modifierPlaces_ShouldSucceed_WhenValueIsValid() {
        // Arrange
        ModifierPlacesCovoiturageDTO request = new ModifierPlacesCovoiturageDTO();
        request.setNbrPlaceRestante(2); // 2 est inférieur à nbrPlaceInitial (3)

        when(covoiturageRepository.findById(1)).thenReturn(Optional.of(covoiturage));
        when(covoiturageRepository.save(any(Covoiturage.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        CovoiturageDTO result = covoiturageService.modifierPlaces(1, request);

        // Assert
        assertNotNull(result);
        verify(covoiturageRepository, times(1)).save(covoiturage);
    }

    @Test
    void modifierPlaces_ShouldThrow400_WhenValueExceedsInitial() {
        // Arrange
        ModifierPlacesCovoiturageDTO request = new ModifierPlacesCovoiturageDTO();
        request.setNbrPlaceRestante(5); // 5 est supérieur à nbrPlaceInitial (3)

        when(covoiturageRepository.findById(1)).thenReturn(Optional.of(covoiturage));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
                () -> covoiturageService.modifierPlaces(1, request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(covoiturageRepository, never()).save(any());
    }

    // ==========================================
    // TESTS DELETE
    // ==========================================

    @Test
    void deleteById_ShouldSucceed_WhenStatusIsNotTermineOrEnCours() {
        // Arrange : statut est PAS_COMMENCER par défaut dans le setUp()
        when(covoiturageRepository.findById(1)).thenReturn(Optional.of(covoiturage));

        // Act
        covoiturageService.deleteById(1);

        // Assert
        verify(covoiturageRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteById_ShouldThrow400_WhenStatusIsEnCours() {
        // Arrange
        covoiturage.setStatut(StatutCovoiturage.EN_COURS);
        when(covoiturageRepository.findById(1)).thenReturn(Optional.of(covoiturage));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
                () -> covoiturageService.deleteById(1));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(covoiturageRepository, never()).deleteById(anyInt());
    }

    @Test
    void create_ShouldThrow400_WhenVehicleAlreadyUsed() {
        // Arrange
        CreerCovoiturageRequest request = createMockCreateRequest(3);
        when(utilisateurContextService.getCurrentUser()).thenReturn(utilisateur);
        when(vehiculeRepository.findById(1)).thenReturn(Optional.of(vehicule));
        
        // On simule que la base de données répond OUI : le véhicule est déjà utilisé !
        when(covoiturageRepository.existsByVehiculeAndDatesOverlapping(anyInt(), any(LocalDateTime.class))).thenReturn(true);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
                () -> covoiturageService.create(request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Ce véhicule est déjà utilisé"));
        verify(covoiturageRepository, never()).save(any(Covoiturage.class));
    }

    // --- Méthodes utilitaires pour créer les fausses requêtes ---

    private CreerCovoiturageRequest createMockCreateRequest(int places) {
        CreerCovoiturageRequest request = new CreerCovoiturageRequest();
        request.setVehiculeId(1);
        request.setNbrPlaceInitial(places);
        request.setDateHeureDebut(LocalDateTime.now().plusDays(5));
        
        AdresseInputDTO adr = new AdresseInputDTO();
        adr.setVille("Montpellier");
        adr.setRue("Avenue d'Occitanie");
        adr.setNumeroRue("10");
        
        request.setAdresseDepart(adr);
        request.setAdresseArrivee(adr);
        return request;
    }
}