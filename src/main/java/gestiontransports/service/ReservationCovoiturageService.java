package gestiontransports.service;
import gestiontransports.repository.ReservationCovoiturageRepository;
import gestiontransports.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gestiontransports.dto.reservation.ReservationCovoiturageDTO;
import gestiontransports.dto.reservation.CreerReservationCovoiturageDTO;
import gestiontransports.dto.reservation.AnnulerReservationCovoiturageDTO;
import gestiontransports.enums.StatutReservation;
import gestiontransports.model.ReservationCovoiturage;
import gestiontransports.model.Utilisateur;
import gestiontransports.model.Covoiturage;
import gestiontransports.adapter.ReservationCovoiturageAdapter;
import gestiontransports.repository.CovoiturageRepository;

@Service
public class ReservationCovoiturageService {
    
        private final ReservationCovoiturageRepository reservationCovoiturageRepository;
        private final UtilisateurContextService utilisateurContextService;
        private final CovoiturageRepository covoiturageRepository;

        public ReservationCovoiturageService(ReservationCovoiturageRepository reservationCovoiturageRepository, UtilisateurRepository utilisateurRepository, UtilisateurContextService utilisateurContextService, CovoiturageRepository covoiturageRepository) {
            this.reservationCovoiturageRepository = reservationCovoiturageRepository;
            this.utilisateurContextService = utilisateurContextService;
            this.covoiturageRepository = covoiturageRepository;
        }

        public ReservationCovoiturageDTO findById(int id) {
            ReservationCovoiturage reservation = reservationCovoiturageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation de covoiturage non trouvée avec l'id : " + id));
            return ReservationCovoiturageAdapter.toDTO(reservation);
        }

        public List<ReservationCovoiturageDTO> findAll() {
            return reservationCovoiturageRepository.findAll().stream()
                .map(ReservationCovoiturageAdapter::toDTO)
                .collect(Collectors.toList());
        }

        @Transactional
        public ReservationCovoiturageDTO create(CreerReservationCovoiturageDTO request) {
            ReservationCovoiturage reservation = new ReservationCovoiturage();

            Utilisateur utilisateur = utilisateurContextService.getCurrentUser();
            Covoiturage covoiturage = covoiturageRepository.findById(request.getCovoiturageId())
            .orElseThrow(() -> new RuntimeException("Covoiturage non trouvé avec l'id : " + request.getCovoiturageId()));

            reservation.setCovoiturage(covoiturage);
            reservation.setUtilisateur(utilisateur);
            reservation.setStatut(StatutReservation.PAS_COMMENCEE);
            ReservationCovoiturage saved = reservationCovoiturageRepository.save(reservation);
            return ReservationCovoiturageAdapter.toDTO(saved);
        }


        @Transactional
        public ReservationCovoiturageDTO annuler(AnnulerReservationCovoiturageDTO request) {
            ReservationCovoiturage reservation = reservationCovoiturageRepository.findById(request.getReservationId())
                .orElseThrow(() -> new RuntimeException("Réservation de covoiturage non trouvée avec l'id : " + request.getReservationId()));
            reservation.setStatut(StatutReservation.ANNULEE);
            ReservationCovoiturage updated = reservationCovoiturageRepository.save(reservation);
            return ReservationCovoiturageAdapter.toDTO(updated);
        }

    @Transactional
    public void delete(int id) {
        if (!reservationCovoiturageRepository.existsById(id)) {
            throw new RuntimeException("Réservation de covoiturage non trouvée avec l'id : " + id);
        }
        reservationCovoiturageRepository.deleteById(id);
    }
}
