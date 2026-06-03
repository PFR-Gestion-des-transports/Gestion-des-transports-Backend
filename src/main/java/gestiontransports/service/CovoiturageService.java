package gestiontransports.service;

import gestiontransports.adapter.CovoiturageAdapter;
import gestiontransports.dto.covoiturage.CovoiturageDTO;
import gestiontransports.repository.UtilisateurRepository;
import gestiontransports.adapter.AdresseAdapter;
import gestiontransports.dto.covoiturage.CreerCovoiturageRequest;   
import gestiontransports.dto.covoiturage.ModifierCovoiturageDTO;
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

@Service
public class CovoiturageService {

    private final CovoiturageRepository covoiturageRepository;
    private final AdresseRepository adresseRepository;
    private final UtilisateurRepository utilisateurRepository;

    public CovoiturageService(CovoiturageRepository covoiturageRepository,
                  AdresseRepository adresseRepository,
                  UtilisateurRepository utilisateurRepository) {
    this.covoiturageRepository = covoiturageRepository;
    this.adresseRepository = adresseRepository;
    this.utilisateurRepository = utilisateurRepository;
    }

    public List<CovoiturageDTO> findAll() {
    return covoiturageRepository.findAll()
        .stream()
        .map(CovoiturageAdapter::toDTO)
        .toList();
    }

    public CovoiturageDTO findById(Integer id) {
    Covoiturage covoiturage = covoiturageRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Covoiturage introuvable"));
    return CovoiturageAdapter.toDTO(covoiturage);
    }

    public CovoiturageDTO create(CreerCovoiturageRequest request) {
    
        Utilisateur utilisateur = utilisateurRepository.findByEmail(SecurityUtils.currentEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
        
        Covoiturage covoiturage = CovoiturageAdapter.toModel(request);

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

    Covoiturage saved = covoiturageRepository.save(covoiturage);
    return CovoiturageAdapter.toDTO(saved);
    }

    public CovoiturageDTO update(Integer id, ModifierCovoiturageDTO request) {
    Covoiturage covoiturage = covoiturageRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Covoiturage introuvable"));


        if(!SecurityUtils.isUserAuthorizedAdminAndSelf(covoiturage.getUtilisateur())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé");
        }

        covoiturage.setNbrPlaceInitial(request.getNbrPlaceInitial());
    covoiturage.setNbrPlaceRestante(request.getNbrPlaceRestante());
    covoiturage.setDateHeureDebut(request.getDateHeureDebut());

    Adresse depart = adresseRepository
        .findByVilleAndRueAndNumeroRue(
            request.getAdresseDepart().getVille(),
            request.getAdresseDepart().getRue(),
            request.getAdresseDepart().getNumeroRue())
        .orElseGet(() -> adresseRepository.save(adresseFromDTO(request.getAdresseDepart())));

    Adresse arrivee = adresseRepository
        .findByVilleAndRueAndNumeroRue(
            request.getAdresseArrivee().getVille(),
            request.getAdresseArrivee().getRue(),
            request.getAdresseArrivee().getNumeroRue())
        .orElseGet(() -> adresseRepository.save(adresseFromDTO(request.getAdresseArrivee())));

    covoiturage.setAdresseDepart(depart);
    covoiturage.setAdresseArrivee(arrivee);

    Covoiturage updated = covoiturageRepository.save(covoiturage);
    return CovoiturageAdapter.toDTO(updated);
    }

    public void deleteById(Integer id) {
    if (!covoiturageRepository.existsById(id)) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Covoiturage introuvable");
    }
    covoiturageRepository.deleteById(id);
    }

    private Adresse adresseFromDTO(gestiontransports.dto.adresse.AdresseDTO dto) {
    Adresse a = new Adresse();
    a.setVille(dto.getVille());
    a.setRue(dto.getRue());
    a.setNumeroRue(dto.getNumeroRue());
    return a;
    }

}
