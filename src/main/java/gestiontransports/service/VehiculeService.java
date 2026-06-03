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
import gestiontransports.repository.UtilisateurRepository;
import gestiontransports.repository.VehiculeRepository;
import gestiontransports.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service gérant les opérations CRUD sur les véhicules, qu'ils soient personnels ou de service.
 * La distinction entre véhicule personnel et de service est portée par le flag {@code estVehiculeService},
 * et les accès en lecture/modification sont restreints au propriétaire ou à un administrateur.
 */
@Service
public class VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final UtilisateurRepository utilisateurRepository;

    public VehiculeService(VehiculeRepository vehiculeRepository,
                           UtilisateurRepository utilisateurRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Enregistre un nouveau véhicule associé à l'utilisateur authentifié.
     * Si la motorisation est électrique, l'émission de CO₂ au kilomètre est automatiquement fixée à 0.
     * La création d'un véhicule de service (flag {@code estVehiculeService = true}) est réservée aux administrateurs.
     *
     * @param request         les informations du véhicule à créer (immatriculation, marque, modèle, motorisation, etc.)
     * @param estVehiculeService {@code true} pour un véhicule appartenant à la flotte de service, {@code false} pour un véhicule personnel
     * @return le DTO du véhicule nouvellement créé
     * @throws org.springframework.web.server.ResponseStatusException 409 si l'immatriculation est déjà enregistrée
     */
    @Transactional
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

    /**
     * Retourne les véhicules personnels disponibles de l'utilisateur authentifié.
     * Seuls les véhicules ayant le statut {@code EN_SERVICE} et n'appartenant pas à la flotte de service sont inclus.
     *
     * @return la liste des véhicules personnels en service appartenant à l'utilisateur courant
     * @throws org.springframework.web.server.ResponseStatusException 404 si l'utilisateur authentifié est introuvable en base
     */
    public List<VehiculeDTO> findVehiculesDisponibles() {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(SecurityUtils.currentEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        return vehiculeRepository
                .findByUtilisateurAndStatutVehiculeAndEstVehiculeService(utilisateur, StatutVehicule.EN_SERVICE, false)
                .stream()
                .map(VehiculeAdapter::toDTO)
                .toList();
    }

    /**
     * Retourne le détail d'un véhicule identifié par son identifiant.
     * L'accès est autorisé uniquement si l'appelant est administrateur ou s'il est le propriétaire du véhicule.
     *
     * @param id l'identifiant du véhicule recherché
     * @return le DTO du véhicule trouvé
     * @throws org.springframework.web.server.ResponseStatusException 404 si le véhicule n'existe pas,
     *         403 si l'appelant n'est pas autorisé à consulter ce véhicule
     */
    public VehiculeDTO findById(int id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Véhicule introuvable"));

        if (!SecurityUtils.isUserAuthorizedAdminAndSelf(vehicule.getUtilisateur())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès interdit");
        }

        return VehiculeAdapter.toDTO(vehicule);
    }

    /**
     * Met à jour les informations d'un véhicule existant.
     * Si la motorisation est modifiée en électrique, le CO₂ au kilomètre est automatiquement remis à 0.
     * L'accès est autorisé uniquement si l'appelant est administrateur ou s'il est le propriétaire du véhicule.
     *
     * @param id      l'identifiant du véhicule à modifier
     * @param request les nouvelles données du véhicule
     * @return le DTO mis à jour du véhicule
     * @throws org.springframework.web.server.ResponseStatusException 404 si le véhicule est introuvable,
     *         403 si l'appelant n'est pas autorisé, 409 si la nouvelle immatriculation est déjà utilisée par un autre véhicule
     */
    @Transactional
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

    /**
     * Modifie uniquement le statut d'un véhicule (par exemple passage de {@code EN_SERVICE} à {@code HORS_SERVICE}).
     * L'accès est autorisé uniquement si l'appelant est administrateur ou s'il est le propriétaire du véhicule.
     *
     * @param id      l'identifiant du véhicule dont le statut doit être modifié
     * @param request le nouveau statut à appliquer
     * @return le DTO du véhicule avec le statut mis à jour
     * @throws org.springframework.web.server.ResponseStatusException 404 si le véhicule est introuvable,
     *         403 si l'appelant n'est pas autorisé à modifier ce véhicule
     */
    public VehiculeDTO modifierStatut(int id, ModifierStatutVehiculeRequestDTO request) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Véhicule introuvable"));

        if (!SecurityUtils.isUserAuthorizedAdminAndSelf(vehicule.getUtilisateur())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès interdit");
        }

        vehicule.setStatutVehicule(request.getStatutVehicule());

        return VehiculeAdapter.toDTO(vehiculeRepository.save(vehicule));
    }

    /**
     * Supprime définitivement un véhicule.
     * La suppression est autorisée uniquement si l'appelant est administrateur ou s'il est le propriétaire du véhicule.
     *
     * @param id l'identifiant du véhicule à supprimer
     * @throws org.springframework.web.server.ResponseStatusException 404 si le véhicule est introuvable,
     *         403 si l'appelant n'est pas autorisé à supprimer ce véhicule
     */
    public void delete(int id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Véhicule introuvable"));

        if (!SecurityUtils.isUserAuthorizedAdminAndSelf(vehicule.getUtilisateur())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès interdit");
        }

        vehiculeRepository.delete(vehicule);
    }
}
