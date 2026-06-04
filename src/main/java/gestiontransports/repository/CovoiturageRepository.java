package gestiontransports.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import gestiontransports.model.Covoiturage;
import java.time.LocalDateTime;


public interface CovoiturageRepository extends JpaRepository<Covoiturage, Integer> {

    Optional<Covoiturage> findByAdresseDepartId(Integer adresseDepartId);
    Optional<Covoiturage> findByAdresseArriveeId(Integer adresseArriveeId);
    Optional<Covoiturage> findByDateHeureDebut(LocalDateTime dateHeureDebut);

    @Query("SELECT COUNT(c) > 0 FROM Covoiturage c WHERE c.vehicule.id = :vehiculeId AND c.dateHeureDebut = :dateHeureDebut")
    boolean existsByVehiculeAndDatesOverlapping(
        @Param("vehiculeId") Integer vehiculeId, 
        @Param("dateHeureDebut") LocalDateTime dateHeureDebut
    );        
}
