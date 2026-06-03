package gestiontransports.repository;

import gestiontransports.enums.StatutReservation;
import gestiontransports.model.ReservationVehicule;
import gestiontransports.model.Utilisateur;
import gestiontransports.model.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationVehiculeRepository extends JpaRepository<ReservationVehicule, Integer> {

    List<ReservationVehicule> findByVehiculeAndStatutReservationIn(Vehicule vehicule, List<StatutReservation> statuts);
    List<ReservationVehicule> findByUtilisateurAndStatutReservationIn(Utilisateur utilisateur, List<StatutReservation> statuts);
}
