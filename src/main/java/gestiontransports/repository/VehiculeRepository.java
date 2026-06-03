package gestiontransports.repository;

import gestiontransports.enums.StatutVehicule;
import gestiontransports.model.Utilisateur;
import gestiontransports.model.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehiculeRepository extends JpaRepository<Vehicule, Integer> {
    List<Vehicule> findByUtilisateurAndStatutVehiculeAndEstVehiculeService(Utilisateur utilisateur, StatutVehicule statutVehicule, Boolean estVehiculeService);
    boolean existsByImmatriculation(String immatriculation);
    boolean existsByImmatriculationAndIdNot(String immatriculation, Integer id);
}
