package gestiontransports.repository;

import gestiontransports.enums.StatutReservation;
import gestiontransports.model.ReservationVehicule;
import gestiontransports.model.Utilisateur;
import gestiontransports.model.Vehicule;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository Spring Data JPA pour l'accès aux réservations de véhicules de service.
 * Fournit les opérations CRUD standard ainsi que des requêtes métier permettant
 * de filtrer les réservations par véhicule ou par collaborateur selon leur statut.
 */
public interface ReservationVehiculeRepository extends JpaRepository<ReservationVehicule, Integer> {

    /**
     * Recherche les réservations d'un véhicule dont le statut figure dans la liste fournie,
     * en posant un verrou pessimiste ({@code SELECT ... FOR UPDATE}) sur les lignes lues.
     * Ce verrou garantit qu'aucune autre transaction concurrente ne peut lire ou modifier
     * ces réservations tant que la transaction courante n'est pas commitée, évitant ainsi
     * les doubles réservations sur le même créneau.
     *
     * @param vehicule le véhicule concerné
     * @param statuts  la liste des statuts à inclure dans la recherche
     * @return la liste des réservations correspondantes
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<ReservationVehicule> findByVehiculeAndStatutReservationIn(Vehicule vehicule, List<StatutReservation> statuts);

    /**
     * Recherche les réservations d'un collaborateur dont le statut figure dans la liste fournie.
     *
     * @param utilisateur le collaborateur concerné
     * @param statuts     la liste des statuts à inclure dans la recherche
     * @return la liste des réservations correspondantes
     */
    List<ReservationVehicule> findByUtilisateurAndStatutReservationIn(Utilisateur utilisateur, List<StatutReservation> statuts);

    /**
     * Passe toutes les réservations dont le statut correspond à {@code source} et dont la date de
     * début est passée au statut {@code cible}. Destinée à être appelée par un scheduler.
     *
     * @param now    la date et heure de référence
     * @param source le statut source (PAS_COMMENCEE)
     * @param cible  le statut cible (COMMENCEE)
     * @return le nombre de réservations mises à jour
     */
    @Modifying
    @Query("UPDATE ReservationVehicule r SET r.statutReservation = :cible " +
           "WHERE r.statutReservation = :source AND r.dateHeureDebut <= :now")
    int demarrerReservationsEchues(@Param("now") LocalDateTime now,
                                   @Param("source") StatutReservation source,
                                   @Param("cible") StatutReservation cible);

    /**
     * Passe toutes les réservations dont le statut correspond à {@code source} et dont la date de
     * fin est passée au statut {@code cible}. Destinée à être appelée par un scheduler.
     *
     * @param now    la date et heure de référence
     * @param source le statut source (COMMENCEE)
     * @param cible  le statut cible (TERMINEE)
     * @return le nombre de réservations mises à jour
     */
    @Modifying
    @Query("UPDATE ReservationVehicule r SET r.statutReservation = :cible " +
           "WHERE r.statutReservation = :source AND r.dateHeureFin <= :now")
    int terminerReservationsEchues(@Param("now") LocalDateTime now,
                                   @Param("source") StatutReservation source,
                                   @Param("cible") StatutReservation cible);
}
