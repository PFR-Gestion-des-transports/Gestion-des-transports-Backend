package gestiontransports.service;

import gestiontransports.adapter.UtilisateurAdapter;
import gestiontransports.dto.utilisateur.ModifierUtilisateurRequestDTO;
import gestiontransports.dto.utilisateur.UtilisateurDTO;
import gestiontransports.model.Adresse;
import gestiontransports.model.Utilisateur;
import gestiontransports.repository.AdresseRepository;
import gestiontransports.repository.UtilisateurRepository;
import gestiontransports.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final AdresseRepository adresseRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository,
                              AdresseRepository adresseRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.adresseRepository = adresseRepository;
    }

    public List<UtilisateurDTO> findAll() {
        return utilisateurRepository.findAll()
                .stream()
                .map(UtilisateurAdapter::toDTO)
                .toList();
    }

    public UtilisateurDTO findById(int id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        if (!SecurityUtils.isUserAuthorizedAdminAndSelf(utilisateur)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès interdit");
        }

        return UtilisateurAdapter.toDTO(utilisateur);
    }

    public UtilisateurDTO update(int id, ModifierUtilisateurRequestDTO request) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        if (!SecurityUtils.isUserAuthorizedAdminAndSelf(utilisateur)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès interdit");
        }

        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setNom(request.getNom());

        Adresse adresse = adresseRepository
                .findByVilleAndRueAndNumeroRue(
                        request.getAdresse().getVille(),
                        request.getAdresse().getRue(),
                        request.getAdresse().getNumeroRue())
                .orElseGet(() -> {
                    Adresse nouvelleAdresse = new Adresse();
                    nouvelleAdresse.setVille(request.getAdresse().getVille());
                    nouvelleAdresse.setRue(request.getAdresse().getRue());
                    nouvelleAdresse.setNumeroRue(request.getAdresse().getNumeroRue());
                    return adresseRepository.save(nouvelleAdresse);
                });
        utilisateur.setAdresse(adresse);

        return UtilisateurAdapter.toDTO(utilisateurRepository.save(utilisateur));
    }

    public void deleteById(int id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable");
        }
        utilisateurRepository.deleteById(id);
    }


}
