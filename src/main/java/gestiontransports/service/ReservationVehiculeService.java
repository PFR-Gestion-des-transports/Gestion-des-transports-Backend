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

/**
 * Service métier gérant le cycle de vie des réservations de véhicules de service.
 * Applique les règles de validation (durée minimale 1 jour, maximale 1 semaine, disponibilité
 * du véhicule) et les contrôles d'autorisation avant toute création, modification ou annulation.
 */
@Service
public class ReservationVehiculeService {

    private static final List<StatutReservation> STATUTS_RESERVATION_ACTIVE =
            List.of(StatutReservation.PAS_COMMENCEE, StatutReservation.COMMENCEE);

    private final ReservationVehiculeRepository reservationVehiculeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final UtilisateurContextService utilisateurContextService;

    public ReservationVehiculeService(ReservationVehiculeRepository reservationVehiculeRepository,
                                      UtilisateurRepository utilisateurRepository,
                                      VehiculeRepository vehiculeRepository,
                                      UtilisateurContextService utilisateurContextService) {
        this.reservationVehiculeRepository = reservationVehiculeRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.utilisateurContextService = utilisateurContextService;
    }

    /**
     * Vérifie qu'aucune réservation active du véhicule ne chevauche la plage demandée.
     *
     * @param vehicule              le véhicule à tester
     * @param dateHeureDebut        début de la plage souhaitée
     * @param dateHeureFin          fin de la plage souhaitée
     * @param excludedReservationId identifiant de la réservation à exclure du test (modification), ou {@code null}
     * @return {@code true} si le véhicule est disponible sur la plage, {@code false} sinon
     */
    private boolean checkDisponibilite(Vehicule vehicule, LocalDateTime dateHeureDebut, LocalDateTime dateHeureFin, Integer excludedReservationId) {
        return reservationVehiculeRepository
                .findByVehiculeAndStatutReservationIn(vehicule, STATUTS_RESERVATION_ACTIVE)
                .stream()
                .filter(r -> excludedReservationId == null || !r.getId().equals(excludedReservationId))
                .noneMatch(r -> r.getDateHeureDebut().isBefore(dateHeureFin)
                        && r.getDateHeureFin().isAfter(dateHeureDebut));
    }

    /**
     * Retourne les réservations actives d'un collaborateur qui chevauchent la plage fournie.
     * Utilisé pour détecter les conflits de covoiturage avant la réservation d'un véhicule.
     *
     * @param utilisateur le collaborateur dont on vérifie les réservations
     * @param dateDebut   début de la plage à tester
     * @param dateFin     fin de la plage à tester
     * @return la liste des réservations actives en conflit
     */
    public List<ReservationVehicule> findActivesByUtilisateurAndOverlap(Utilisateur utilisateur, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return reservationVehiculeRepository
                .findByUtilisateurAndStatutReservationIn(utilisateur, STATUTS_RESERVATION_ACTIVE)
                .stream()
                .filter(r -> r.getDateHeureDebut().isBefore(dateFin) && r.getDateHeureFin().isAfter(dateDebut))
                .toList();
    }

    /**
     * Valide l'ensemble des règles métier avant création ou modification d'une réservation :
     * le véhicule doit être de service, la date de début ne doit pas être passée,
     * la durée doit être comprise entre 1 jour et 1 semaine, et le véhicule doit être disponible.
     *
     * @param vehicule              le véhicule concerné
     * @param dateHeureDebut        date et heure de début souhaitées
     * @param dateHeureFin          date et heure de fin souhaitées
     * @param excludedReservationId identifiant de la réservation à ignorer lors du contrôle de disponibilité, ou {@code null}
     * @throws ResponseStatusException si une règle métier est violée
     */
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

    /**
     * Retourne les réservations actives (statut {@code PAS_COMMENCEE} ou {@code COMMENCEE})
     * pour le véhicule identifié.
     *
     * @param vehiculeId l'identifiant du véhicule
     * @return la liste des DTOs de réservations actives
     * @throws ResponseStatusException 404 si le véhicule est introuvable
     */
    public List<ReservationVehiculeDTO> findActivesByVehiculeId(int vehiculeId) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Véhicule introuvable"));

        return reservationVehiculeRepository
                .findByVehiculeAndStatutReservationIn(vehicule, STATUTS_RESERVATION_ACTIVE)
                .stream()
                .map(ReservationVehiculeAdapter::toDTO)
                .toList();
    }

    /**
     * Modifie les dates d'une réservation au statut {@code PAS_COMMENCEE}.
     * Vérifie que l'utilisateur connecté est le propriétaire de la réservation ou un administrateur.
     *
     * @param id      l'identifiant de la réservation à modifier
     * @param request le DTO contenant les nouvelles dates
     * @return le DTO de la réservation mise à jour
     * @throws ResponseStatusException 404 si la réservation ou l'utilisateur est introuvable,
     *                                 403 si l'accès est interdit, 400 si les règles métier sont violées
     */
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

    /**
     * Annule une réservation au statut {@code PAS_COMMENCEE}.
     * Une réservation en cours, terminée ou déjà annulée ne peut pas être annulée.
     * Vérifie que l'utilisateur connecté est le propriétaire de la réservation ou un administrateur.
     *
     * @param id l'identifiant de la réservation à annuler
     * @throws ResponseStatusException 404 si la réservation ou l'utilisateur est introuvable,
     *                                 403 si l'accès est interdit, 400 si le statut ne permet pas l'annulation
     */
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

    /**
     * Crée une nouvelle réservation de véhicule de service pour le collaborateur actuellement connecté.
     * Applique toutes les validations métier avant la persistance.
     *
     * @param request le DTO contenant le véhicule et les dates souhaitées
     * @return le DTO de la réservation créée
     * @throws ResponseStatusException 404 si l'utilisateur ou le véhicule est introuvable,
     *                                 400 si les règles métier sont violées
     */
    @Transactional
    public ReservationVehiculeDTO create(ReserverVehiculeServiceDTO request) {
        Utilisateur utilisateur = utilisateurContextService.getCurrentUser();

        Vehicule vehicule = vehiculeRepository.findById(request.getVehiculeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Véhicule introuvable"));

        validerDatesEtDisponibilite(vehicule, request.getDateHeureDebut(), request.getDateHeureFin(), null);

        return ReservationVehiculeAdapter.toDTO(reservationVehiculeRepository.save(ReservationVehiculeAdapter.toModel(request, utilisateur, vehicule)));
    }
}
