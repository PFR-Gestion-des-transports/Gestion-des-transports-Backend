package gestiontransports.service;

import gestiontransports.adapter.CovoiturageAdapter;
import gestiontransports.dto.covoiturage.CovoiturageDTO;
import gestiontransports.repository.UtilisateurRepository;
import gestiontransports.model.Vehicule;
import gestiontransports.enums.StatutCovoiturage;
import gestiontransports.adapter.AdresseAdapter;
import gestiontransports.dto.covoiturage.CreerCovoiturageRequest;   
import gestiontransports.dto.covoiturage.ModifierCovoiturageDTO;
import gestiontransports.repository.VehiculeRepository;
import gestiontransports.security.SecurityUtils;
import gestiontransports.model.Adresse;
import gestiontransports.model.Covoiturage;
import gestiontransports.model.Utilisateur;
import gestiontransports.repository.AdresseRepository;
import gestiontransports.repository.CovoiturageRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service gérant les opérations métier sur les annonces de covoiturage d'entreprise.
 * Il assure la création, la consultation, la modification et la suppression des covoiturages,
 * en contrôlant que seul le propriétaire de l'annonce ou un administrateur peut la modifier ou la supprimer.
 */
@Service
public class CovoiturageService {

    private final CovoiturageRepository covoiturageRepository;
    private final AdresseRepository adresseRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final VehiculeRepository vehiculeRepository;

    public CovoiturageService(CovoiturageRepository covoiturageRepository,
                  AdresseRepository adresseRepository,
                  UtilisateurRepository utilisateurRepository,
                  VehiculeRepository vehiculeRepository) {
    this.covoiturageRepository = covoiturageRepository;
    this.adresseRepository = adresseRepository;
    this.utilisateurRepository = utilisateurRepository;
    this.vehiculeRepository = vehiculeRepository;
    }

    /**
     * Retourne la liste complète des covoiturages enregistrés dans le système.
     *
     * @return la liste de tous les covoiturages sous forme de DTOs
     */
    public List<CovoiturageDTO> findAll() {
    return covoiturageRepository.findAll()
        .stream()
        .map(CovoiturageAdapter::toDTO)
        .toList();
    }

    /**
     * Retourne le détail d'un covoiturage identifié par son identifiant.
     *
     * @param id l'identifiant du covoiturage recherché
     * @return le DTO du covoiturage trouvé
     * @throws org.springframework.web.server.ResponseStatusException 404 si aucun covoiturage ne correspond à cet identifiant
     */
    public CovoiturageDTO findById(int id) {
    Covoiturage covoiturage = covoiturageRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Covoiturage introuvable"));
    return CovoiturageAdapter.toDTO(covoiturage);
    }

    /**
     * Crée une nouvelle annonce de covoiturage pour l'utilisateur authentifié.
     * Les adresses de départ et d'arrivée sont réutilisées si elles existent déjà en base,
     * sinon elles sont créées. Le covoiturage est initialisé avec le statut {@code PAS_COMMENCER}.
     *
     * @param request les informations de l'annonce à créer (dates, adresses, nombre de places, identifiant du véhicule)
     * @return le DTO du covoiturage nouvellement créé
     * @throws org.springframework.web.server.ResponseStatusException 404 si l'utilisateur authentifié ou le véhicule est introuvable
     */
    public CovoiturageDTO create(CreerCovoiturageRequest request) {
    
        Utilisateur utilisateur = utilisateurRepository.findByEmail(SecurityUtils.currentEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
        
        Covoiturage covoiturage = CovoiturageAdapter.toModel(request);

        Vehicule vehicule = vehiculeRepository.findById(request.getVehiculeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Véhicule introuvable"));

    Adresse depart = adresseRepository
        .findByVilleAndRueAndNumeroRue(
            request.getAdresseDepart().getVille(),
            request.getAdresseDepart().getRue(),
            request.getAdresseDepart().getNumeroRue())
        .orElseGet(() -> adresseRepository.save(AdresseAdapter.toModel(request.getAdresseDepart())));

    Adresse arrivee = adresseRepository
        .findByVilleAndRueAndNumeroRue(
            request.getAdresseArrivee().getVille(),
            request.getAdresseArrivee().getRue(),
            request.getAdresseArrivee().getNumeroRue())
        .orElseGet(() -> adresseRepository.save(AdresseAdapter.toModel(request.getAdresseArrivee())));

    covoiturage.setAdresseDepart(depart);
    covoiturage.setAdresseArrivee(arrivee);
    covoiturage.setUtilisateur(utilisateur);
    covoiturage.setVehicule(vehicule);
    covoiturage.setStatut(StatutCovoiturage.PAS_COMMENCER);

    Covoiturage saved = covoiturageRepository.save(covoiturage);
    return CovoiturageAdapter.toDTO(saved);
    }

    /**
     * Met à jour une annonce de covoiturage existante.
     * La modification est refusée si le covoiturage a le statut {@code TERMINE} ou si l'appelant
     * n'est ni le propriétaire de l'annonce ni un administrateur.
     *
     * @param id      l'identifiant du covoiturage à modifier
     * @param request les nouvelles données de l'annonce (places, dates, adresses, véhicule, statut)
     * @return le DTO mis à jour du covoiturage
     * @throws org.springframework.web.server.ResponseStatusException 404 si le covoiturage ou le véhicule est introuvable,
     *         403 si l'appelant n'est pas autorisé, 400 si le covoiturage est déjà terminé
     */
    public CovoiturageDTO update(int id, ModifierCovoiturageDTO request) {
    Covoiturage covoiturage = covoiturageRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Covoiturage introuvable"));


        if(!SecurityUtils.isUserAuthorizedAdminAndSelf(covoiturage.getUtilisateur())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé");
        }

        if(covoiturage.getStatut() == gestiontransports.enums.StatutCovoiturage.TERMINE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible de modifier un covoiturage terminé");
        }

        covoiturage.setNbrPlaceInitial(request.getNbrPlaceInitial());
    covoiturage.setNbrPlaceRestante(request.getNbrPlaceRestante());
    covoiturage.setDateHeureDebut(request.getDateHeureDebut());

    Adresse depart = adresseRepository
        .findByVilleAndRueAndNumeroRue(
            request.getAdresseDepart().getVille(),
            request.getAdresseDepart().getRue(),
            request.getAdresseDepart().getNumeroRue())
        .orElseGet(() -> adresseRepository.save(AdresseAdapter.toModel(request.getAdresseDepart())));

    Adresse arrivee = adresseRepository
        .findByVilleAndRueAndNumeroRue(
            request.getAdresseArrivee().getVille(),
            request.getAdresseArrivee().getRue(),
            request.getAdresseArrivee().getNumeroRue())
        .orElseGet(() -> adresseRepository.save(AdresseAdapter.toModel(request.getAdresseArrivee())));

    Vehicule vehicule = vehiculeRepository.findById(request.getVehiculeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Véhicule introuvable"));

    covoiturage.setAdresseDepart(depart);
    covoiturage.setAdresseArrivee(arrivee);
    covoiturage.setVehicule(vehicule);
    covoiturage.setStatut(request.getStatut());
    Covoiturage updated = covoiturageRepository.save(covoiturage);
    return CovoiturageAdapter.toDTO(updated);
    }

    /**
     * Supprime définitivement une annonce de covoiturage.
     * La suppression est autorisée uniquement si l'appelant est administrateur ou s'il est le propriétaire de l'annonce.
     *
     * @param id l'identifiant du covoiturage à supprimer
     * @throws org.springframework.web.server.ResponseStatusException 404 si le covoiturage est introuvable,
     *         403 si l'appelant n'est pas autorisé à supprimer cette annonce
     */
    public void deleteById(int id) {
        Covoiturage covoiturage = covoiturageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Covoiturage introuvable"));

        if (!SecurityUtils.isUserAuthorizedAdminAndSelf(covoiturage.getUtilisateur())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé");
        }

        covoiturageRepository.deleteById(id);
    }

}
