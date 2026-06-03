package gestiontransports.repository;

import gestiontransports.enums.StatutVehicule;
import gestiontransports.model.Utilisateur;
import gestiontransports.model.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository JPA pour l'entité {@link Vehicule}.
 * Offre des requêtes de filtrage multi-critères et des vérifications d'unicité
 * sur l'immatriculation, en complément des opérations CRUD standard.
 */
public interface VehiculeRepository extends JpaRepository<Vehicule, Integer> {

    /**
     * Retourne les véhicules d'un utilisateur filtrés par statut et par type (service ou personnel).
     * Utilisé notamment pour lister les véhicules disponibles pour un covoiturage.
     *
     * @param utilisateur        le propriétaire du véhicule
     * @param statutVehicule     le statut souhaité (ex. : DISPONIBLE)
     * @param estVehiculeService {@code true} pour les véhicules de service, {@code false} pour les personnels
     * @return la liste des véhicules correspondant aux critères, vide si aucun résultat
     */
    List<Vehicule> findByUtilisateurAndStatutVehiculeAndEstVehiculeService(Utilisateur utilisateur, StatutVehicule statutVehicule, Boolean estVehiculeService);

    /**
     * Vérifie si un véhicule avec cette immatriculation existe déjà en base.
     * Utilisé lors de la création pour garantir l'unicité de l'immatriculation.
     *
     * @param immatriculation le numéro d'immatriculation à vérifier
     * @return {@code true} si un véhicule avec cette immatriculation existe, {@code false} sinon
     */
    boolean existsByImmatriculation(String immatriculation);

    /**
     * Vérifie si un autre véhicule (différent de celui identifié par {@code id}) possède déjà
     * cette immatriculation. Utilisé lors de la mise à jour pour détecter les conflits.
     *
     * @param immatriculation le numéro d'immatriculation à vérifier
     * @param id              l'identifiant du véhicule exclu de la vérification
     * @return {@code true} si un autre véhicule a la même immatriculation, {@code false} sinon
     */
    boolean existsByImmatriculationAndIdNot(String immatriculation, Integer id);
}
