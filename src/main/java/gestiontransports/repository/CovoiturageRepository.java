package gestiontransports.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import gestiontransports.model.Covoiturage;
import java.time.LocalDateTime;
import java.util.List;


public interface CovoiturageRepository extends JpaRepository<Covoiturage, Integer> {

    List<Covoiturage> findByAdresseDepartId(Integer adresseDepartId);
    List<Covoiturage> findByAdresseArriveeId(Integer adresseArriveeId);
    List<Covoiturage> findByDateHeureDebut(LocalDateTime dateHeureDebut);

}
