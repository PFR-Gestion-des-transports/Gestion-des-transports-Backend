package gestiontransports.service;

import gestiontransports.enums.StatutReservation;
import gestiontransports.repository.ReservationVehiculeRepository;
import gestiontransports.repository.VehiculeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
class ReservationVehiculeSchedulerTest {

    @Mock
    private ReservationVehiculeRepository reservationVehiculeRepository;

    @Mock
    private VehiculeRepository vehiculeRepository;

    @Mock
    private UtilisateurContextService utilisateurContextService;

    @InjectMocks
    private ReservationVehiculeService service;

    @Test
    void mettreAJourStatutsReservations_demarre_puis_termine_dans_lordre() {
        service.mettreAJourStatutsReservations();

        var ordre = inOrder(reservationVehiculeRepository);
        ordre.verify(reservationVehiculeRepository).demarrerReservationsEchues(
                any(LocalDateTime.class),
                eq(StatutReservation.PAS_COMMENCEE),
                eq(StatutReservation.COMMENCEE));
        ordre.verify(reservationVehiculeRepository).terminerReservationsEchues(
                any(LocalDateTime.class),
                eq(StatutReservation.COMMENCEE),
                eq(StatutReservation.TERMINEE));
    }
}
