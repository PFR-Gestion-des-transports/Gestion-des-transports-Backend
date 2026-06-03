package gestiontransports.repository;

import gestiontransports.enums.StatutReservation;
import gestiontransports.model.ReservationVehicule;
import gestiontransports.model.Utilisateur;
import gestiontransports.model.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository Spring Data JPA pour l'accès aux réservations de véhicules de service.
 * Fournit les opérations CRUD standard ainsi que des requêtes métier permettant
 * de filtrer les réservations par véhicule ou par collaborateur selon leur statut.
 */
public interface ReservationVehiculeRepository extends JpaRepository<ReservationVehicule, Integer> {

    /**
     * Recherche les réservations d'un véhicule dont le statut figure dans la liste fournie.
     * Utilisé notamment pour détecter les conflits de disponibilité avant création d'une réservation.
     *
     * @param vehicule le véhicule concerné
     * @param statuts  la liste des statuts à inclure dans la recherche
     * @return la liste des réservations correspondantes
     */
    List<ReservationVehicule> findByVehiculeAndStatutReservationIn(Vehicule vehicule, List<StatutReservation> statuts);

    /**
     * Recherche les réservations d'un collaborateur dont le statut figure dans la liste fournie.
     *
     * @param utilisateur le collaborateur concerné
     * @param statuts     la liste des statuts à inclure dans la recherche
     * @return la liste des réservations correspondantes
     */
    List<ReservationVehicule> findByUtilisateurAndStatutReservationIn(Utilisateur utilisateur, List<StatutReservation> statuts);
}
