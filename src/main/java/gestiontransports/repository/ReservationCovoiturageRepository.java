package gestiontransports.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import gestiontransports.model.ReservationCovoiturage;

public interface ReservationCovoiturageRepository extends JpaRepository<ReservationCovoiturage, Integer> {
    
}
