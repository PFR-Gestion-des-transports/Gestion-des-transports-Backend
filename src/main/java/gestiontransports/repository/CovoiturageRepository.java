package gestiontransports.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import gestiontransports.model.Covoiturage;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Repository JPA pour l'entité {@link Covoiturage}.
 * Propose des méthodes de recherche par adresse de départ, d'arrivée ou date de départ,
 * en plus des opérations CRUD standard héritées de {@link JpaRepository}.
 */
public interface CovoiturageRepository extends JpaRepository<Covoiturage, Integer> {

    /**
     * Retourne tous les covoiturages partant d'une adresse donnée.
     *
     * @param adresseDepartId l'identifiant de l'adresse de départ
     * @return la liste des covoiturages correspondants, vide si aucun résultat
     */
    List<Covoiturage> findByAdresseDepartId(Integer adresseDepartId);

    /**
     * Retourne tous les covoiturages à destination d'une adresse donnée.
     *
     * @param adresseArriveeId l'identifiant de l'adresse d'arrivée
     * @return la liste des covoiturages correspondants, vide si aucun résultat
     */
    List<Covoiturage> findByAdresseArriveeId(Integer adresseArriveeId);

    /**
     * Retourne tous les covoiturages planifiés à une date et heure de départ exactes.
     *
     * @param dateHeureDebut la date et heure de départ à rechercher
     * @return la liste des covoiturages correspondants, vide si aucun résultat
     */
    List<Covoiturage> findByDateHeureDebut(LocalDateTime dateHeureDebut);

}
