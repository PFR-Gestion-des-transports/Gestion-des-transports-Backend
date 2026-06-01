package gestiontransports.repository;

import gestiontransports.model.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdresseRepository extends JpaRepository<Adresse, Integer> {
    Optional<Adresse> findByVilleAndRueAndNumeroRue(String ville, String rue, String numeroRue);
}
