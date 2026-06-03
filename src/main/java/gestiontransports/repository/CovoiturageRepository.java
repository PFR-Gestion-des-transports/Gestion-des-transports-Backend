package gestiontransports.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import gestiontransports.model.Covoiturage;
import java.time.LocalDateTime;


public interface CovoiturageRepository extends JpaRepository<Covoiturage, Integer> {

    Optional<Covoiturage> findByAdresseDepartId(Integer adresseDepartId);
    Optional<Covoiturage> findByAdresseArriveeId(Integer adresseArriveeId);
    Optional<Covoiturage> findByDateHeureDebut(LocalDateTime dateHeureDebut);
        
}
