package gestiontransports.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import gestiontransports.adapter.CovoiturageAdapter;
import gestiontransports.dto.covoiturage.CovoiturageDTO;
import gestiontransports.dto.covoiturage.ModifierPlacesCovoiturageDTO;
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
import org.springframework.transaction.annotation.Transactional;
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
    private final VehiculeRepository vehiculeRepository;
    private final UtilisateurContextService utilisateurContextService;

    public CovoiturageService(CovoiturageRepository covoiturageRepository,
                  AdresseRepository adresseRepository,
                  VehiculeRepository vehiculeRepository,
                  UtilisateurContextService utilisateurContextService) {
    this.covoiturageRepository = covoiturageRepository;
    this.adresseRepository = adresseRepository;
    this.vehiculeRepository = vehiculeRepository;
    this.utilisateurContextService = utilisateurContextService;
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
    @Transactional
    public CovoiturageDTO create(CreerCovoiturageRequest request) {
    
        Utilisateur utilisateur = utilisateurContextService.getCurrentUser();
        
        Covoiturage covoiturage = CovoiturageAdapter.toModel(request);

        Vehicule vehicule = vehiculeRepository.findById(request.getVehiculeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Véhicule introuvable"));

        if(isAlreadyUseCar(vehicule, request.getDateHeureDebut())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce véhicule est déjà utilisé pour un covoiturage à cette date et heure");
        }

        if (request.getNbrPlaceInitial() > vehicule.getNombreDePlace()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le nombre de places initial ne peut pas dépasser la capacité du véhicule (" + vehicule.getNombreDePlace() + ")");
        }

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
    @Transactional
    public CovoiturageDTO update(int id, ModifierCovoiturageDTO request) {

        Covoiturage covoiturage = covoiturageRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Covoiturage introuvable"));


        if(!SecurityUtils.isUserAuthorizedAdminAndSelf(covoiturage.getUtilisateur())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé");
        }

        if(covoiturage.getStatut() == gestiontransports.enums.StatutCovoiturage.TERMINE || covoiturage.getStatut() == gestiontransports.enums.StatutCovoiturage.EN_COURS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible de modifier un covoiturage terminé");
        }

        if(isBeetwenOneDay(covoiturage.getDateHeureDebut())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible de modifier un covoiturage déjà commencé");
        }

        covoiturage.setNbrPlaceInitial(request.getNbrPlaceInitial());
        covoiturage.setDateHeureDebut(request.getDateHeureDebut());

        covoiturage.setNbrPlaceInitial(request.getNbrPlaceInitial());
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

        if(isAlreadyUseCar(vehicule, request.getDateHeureDebut())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce véhicule est déjà utilisé pour un covoiturage à cette date et heure");
        }

        if (request.getNbrPlaceInitial() > vehicule.getNombreDePlace()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le nombre de places initial ne peut pas dépasser la capacité du véhicule (" + vehicule.getNombreDePlace() + ")");
        }

        covoiturage.setAdresseDepart(depart);
        covoiturage.setAdresseArrivee(arrivee);
        covoiturage.setVehicule(vehicule);
        covoiturage.setStatut(request.getStatut());
        Covoiturage updated = covoiturageRepository.save(covoiturage);
        return CovoiturageAdapter.toDTO(updated);
    }

    /**
     * Met à jour le nombre de places restantes d'un covoiturage.
     * Permet au conducteur de signaler qu'un passager l'a rejoint hors application.
     * La valeur doit être comprise entre 0 et {@code nbrPlaceInitial}.
     *
     * @param id      l'identifiant du covoiturage à modifier
     * @param request le DTO contenant le nouveau nombre de places restantes
     * @return le DTO mis à jour du covoiturage
     * @throws org.springframework.web.server.ResponseStatusException 404 si le covoiturage est introuvable,
     *         403 si l'appelant n'est pas autorisé, 400 si la valeur dépasse {@code nbrPlaceInitial}
     */
    @Transactional
    public CovoiturageDTO modifierPlaces(int id, ModifierPlacesCovoiturageDTO request) {
        Covoiturage covoiturage = covoiturageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Covoiturage introuvable"));

        if (!SecurityUtils.isUserAuthorizedAdminAndSelf(covoiturage.getUtilisateur())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé");
        }

        if (request.getNbrPlaceRestante() > covoiturage.getNbrPlaceInitial()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le nombre de places restantes ne peut pas dépasser le nombre de places initiales ("
                            + covoiturage.getNbrPlaceInitial() + ")");
        }

        covoiturage.setNbrPlaceRestante(request.getNbrPlaceRestante());
        return CovoiturageAdapter.toDTO(covoiturageRepository.save(covoiturage));
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
        
        if(covoiturage.getStatut() == gestiontransports.enums.StatutCovoiturage.TERMINE || covoiturage.getStatut() == gestiontransports.enums.StatutCovoiturage.EN_COURS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible de supprimer un covoiturage terminé ou en cours");
        }

        covoiturageRepository.deleteById(id);
    }


    private boolean isBeetwenOneDay(LocalDateTime dateHeureDebut) {
        return ChronoUnit.DAYS.between(LocalDateTime.now(), dateHeureDebut) < 1;
    }

    private boolean isAlreadyUseCar(Vehicule vehicule, LocalDateTime dateHeureDebut) {
        return covoiturageRepository.existsByVehiculeAndDatesOverlapping(vehicule.getId(), dateHeureDebut);
    }





}
