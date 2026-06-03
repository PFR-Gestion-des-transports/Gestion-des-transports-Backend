package gestiontransports.service;

import gestiontransports.adapter.ReservationVehiculeAdapter;
import gestiontransports.dto.reservationvehicule.ModifierReservationVehiculeDTO;
import gestiontransports.dto.reservationvehicule.ReservationVehiculeDTO;
import gestiontransports.dto.reservationvehicule.ReserverVehiculeServiceDTO;
import gestiontransports.enums.StatutReservation;
import gestiontransports.model.Utilisateur;
import gestiontransports.model.Vehicule;
import gestiontransports.model.ReservationVehicule;
import gestiontransports.repository.ReservationVehiculeRepository;
import gestiontransports.repository.UtilisateurRepository;
import gestiontransports.repository.VehiculeRepository;
import gestiontransports.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationVehiculeService {

    private static final List<StatutReservation> STATUTS_RESERVATION_ACTIVE =
            List.of(StatutReservation.PAS_COMMENCEE, StatutReservation.COMMENCEE);

    private final ReservationVehiculeRepository reservationVehiculeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final VehiculeRepository vehiculeRepository;

    public ReservationVehiculeService(ReservationVehiculeRepository reservationVehiculeRepository,
                                      UtilisateurRepository utilisateurRepository,
                                      VehiculeRepository vehiculeRepository) {
        this.reservationVehiculeRepository = reservationVehiculeRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.vehiculeRepository = vehiculeRepository;
    }

    private boolean checkDisponibilite(Vehicule vehicule, LocalDateTime dateHeureDebut, LocalDateTime dateHeureFin, Integer excludedReservationId) {
        return reservationVehiculeRepository
                .findByVehiculeAndStatutReservationIn(vehicule, STATUTS_RESERVATION_ACTIVE)
                .stream()
                .filter(r -> excludedReservationId == null || !r.getId().equals(excludedReservationId))
                .noneMatch(r -> r.getDateHeureDebut().isBefore(dateHeureFin)
                        && r.getDateHeureFin().isAfter(dateHeureDebut));
    }

    public List<ReservationVehicule> findActivesByUtilisateurAndOverlap(Utilisateur utilisateur, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return reservationVehiculeRepository
                .findByUtilisateurAndStatutReservationIn(utilisateur, STATUTS_RESERVATION_ACTIVE)
                .stream()
                .filter(r -> r.getDateHeureDebut().isBefore(dateFin) && r.getDateHeureFin().isAfter(dateDebut))
                .toList();
    }

    private void validerDatesEtDisponibilite(Vehicule vehicule, LocalDateTime dateHeureDebut, LocalDateTime dateHeureFin, Integer excludedReservationId) {
        if (!vehicule.isEstVehiculeService()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce véhicule n'est pas un véhicule de service");
        }
        if (dateHeureDebut.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La date de début ne peut pas être antérieure à la date actuelle");
        }
        if (dateHeureFin.isBefore(dateHeureDebut.plusDays(1))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La durée minimale de réservation est d'une journée");
        }
        if (dateHeureFin.isAfter(dateHeureDebut.plusWeeks(1))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La durée maximale de réservation est d'une semaine");
        }
        if (!checkDisponibilite(vehicule, dateHeureDebut, dateHeureFin, excludedReservationId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le véhicule n'est pas disponible pour ces dates");
        }
    }

    public List<ReservationVehiculeDTO> findActivesByVehiculeId(int vehiculeId) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Véhicule introuvable"));

        return reservationVehiculeRepository
                .findByVehiculeAndStatutReservationIn(vehicule, STATUTS_RESERVATION_ACTIVE)
                .stream()
                .map(ReservationVehiculeAdapter::toDTO)
                .toList();
    }

    @Transactional
    public ReservationVehiculeDTO update(int id, ModifierReservationVehiculeDTO request) {
        ReservationVehicule reservation = reservationVehiculeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Réservation introuvable"));

        Utilisateur utilisateur = utilisateurRepository.findById(reservation.getUtilisateur().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        if (!SecurityUtils.isUserAuthorizedAdminAndSelf(utilisateur)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès interdit");
        }

        if (reservation.getStatutReservation() != StatutReservation.PAS_COMMENCEE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seule une réservation non commencée peut être modifiée");
        }

        validerDatesEtDisponibilite(reservation.getVehicule(), request.getDateHeureDebut(), request.getDateHeureFin(), reservation.getId());

        reservation.setDateHeureDebut(request.getDateHeureDebut());
        reservation.setDateHeureFin(request.getDateHeureFin());
        return ReservationVehiculeAdapter.toDTO(reservationVehiculeRepository.save(reservation));
    }

    @Transactional
    public void annuler(int id) {
        ReservationVehicule reservation = reservationVehiculeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Réservation introuvable"));

        Utilisateur utilisateur = utilisateurRepository.findById(reservation.getUtilisateur().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        if (!SecurityUtils.isUserAuthorizedAdminAndSelf(utilisateur)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès interdit");
        }

        switch (reservation.getStatutReservation()) {
            case COMMENCEE -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible d'annuler une réservation en cours");
            case TERMINEE -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible d'annuler une réservation terminée");
            case ANNULEE -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La réservation est déjà annulée");
            default -> {}
        }

        reservation.setStatutReservation(StatutReservation.ANNULEE);
        reservationVehiculeRepository.save(reservation);
    }

    @Transactional
    public ReservationVehiculeDTO create(ReserverVehiculeServiceDTO request) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(SecurityUtils.currentEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        Vehicule vehicule = vehiculeRepository.findById(request.getVehiculeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Véhicule introuvable"));

        validerDatesEtDisponibilite(vehicule, request.getDateHeureDebut(), request.getDateHeureFin(), null);

        return ReservationVehiculeAdapter.toDTO(reservationVehiculeRepository.save(ReservationVehiculeAdapter.toModel(request, utilisateur, vehicule)));
    }
}
