package gestiontransports.service;

import gestiontransports.adapter.VehiculeAdapter;
import gestiontransports.dto.vehicule.CreerVehiculeRequestDTO;
import gestiontransports.dto.vehicule.ModifierStatutVehiculeRequestDTO;
import gestiontransports.dto.vehicule.ModifierVehiculeRequestDTO;
import gestiontransports.dto.vehicule.VehiculeDTO;
import gestiontransports.enums.Motorisation;
import gestiontransports.enums.StatutVehicule;
import gestiontransports.model.Utilisateur;
import gestiontransports.model.Vehicule;

import java.util.List;
import gestiontransports.repository.UtilisateurRepository;
import gestiontransports.repository.VehiculeRepository;
import gestiontransports.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final UtilisateurRepository utilisateurRepository;

    public VehiculeService(VehiculeRepository vehiculeRepository,
                           UtilisateurRepository utilisateurRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public VehiculeDTO create(CreerVehiculeRequestDTO request, boolean estVehiculeService) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(SecurityUtils.currentEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        if (vehiculeRepository.existsByImmatriculation(request.getImmatriculation())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Immatriculation déjà utilisée");
        }

        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation(request.getImmatriculation());
        vehicule.setMarque(request.getMarque());
        vehicule.setModele(request.getModele());
        vehicule.setUrlPhoto(request.getUrlPhoto());
        vehicule.setCo2Km(request.getMotorisation() == Motorisation.ELECTRIQUE ? 0.0 : request.getCo2Km());
        vehicule.setNombreDePlace(request.getNombreDePlace());
        vehicule.setCategorie(request.getCategorie());
        vehicule.setMotorisation(request.getMotorisation());
        vehicule.setEstVehiculeService(estVehiculeService);
        vehicule.setStatutVehicule(request.getStatutVehicule());
        vehicule.setUtilisateur(utilisateur);

        return VehiculeAdapter.toDTO(vehiculeRepository.save(vehicule));
    }

    public List<VehiculeDTO> findVehiculesDisponibles() {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(SecurityUtils.currentEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        return vehiculeRepository
                .findByUtilisateurAndStatutVehiculeAndEstVehiculeService(utilisateur, StatutVehicule.EN_SERVICE, false)
                .stream()
                .map(VehiculeAdapter::toDTO)
                .toList();
    }

    public VehiculeDTO findById(int id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Véhicule introuvable"));

        if (!SecurityUtils.isUserAuthorizedAdminAndSelf(vehicule.getUtilisateur())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès interdit");
        }

        return VehiculeAdapter.toDTO(vehicule);
    }

    public VehiculeDTO update(int id, ModifierVehiculeRequestDTO request) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Véhicule introuvable"));

        if (!SecurityUtils.isUserAuthorizedAdminAndSelf(vehicule.getUtilisateur())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès interdit");
        }

        if (vehiculeRepository.existsByImmatriculationAndIdNot(request.getImmatriculation(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Immatriculation déjà utilisée");
        }

        vehicule.setImmatriculation(request.getImmatriculation());
        vehicule.setMarque(request.getMarque());
        vehicule.setModele(request.getModele());
        vehicule.setUrlPhoto(request.getUrlPhoto());
        vehicule.setCo2Km(request.getMotorisation() == Motorisation.ELECTRIQUE ? 0.0 : request.getCo2Km());
        vehicule.setNombreDePlace(request.getNombreDePlace());
        vehicule.setCategorie(request.getCategorie());
        vehicule.setMotorisation(request.getMotorisation());
        vehicule.setEstVehiculeService(request.getEstVehiculeService());
        vehicule.setStatutVehicule(request.getStatutVehicule());

        return VehiculeAdapter.toDTO(vehiculeRepository.save(vehicule));
    }

    public VehiculeDTO modifierStatut(int id, ModifierStatutVehiculeRequestDTO request) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Véhicule introuvable"));

        if (!SecurityUtils.isUserAuthorizedAdminAndSelf(vehicule.getUtilisateur())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès interdit");
        }

        vehicule.setStatutVehicule(request.getStatutVehicule());

        return VehiculeAdapter.toDTO(vehiculeRepository.save(vehicule));
    }

    public void delete(int id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Véhicule introuvable"));

        if (!SecurityUtils.isUserAuthorizedAdminAndSelf(vehicule.getUtilisateur())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès interdit");
        }

        vehiculeRepository.deleteById(id);
    }
}
