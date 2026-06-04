package gestiontransports.service;

import gestiontransports.adapter.AdresseAdapter;
import gestiontransports.adapter.UtilisateurAdapter;
import gestiontransports.dto.utilisateur.ModifierUtilisateurRequestDTO;
import gestiontransports.dto.utilisateur.UtilisateurDTO;
import gestiontransports.model.Adresse;
import gestiontransports.model.Utilisateur;
import gestiontransports.repository.AdresseRepository;
import gestiontransports.aop.RequiresAdminOrSelf;
import gestiontransports.repository.UtilisateurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service gérant les opérations CRUD sur les comptes utilisateurs.
 * L'accès aux données d'un utilisateur est restreint à l'utilisateur lui-même ou à un administrateur,
 * conformément à la politique d'autorisation {@code isUserAuthorizedAdminAndSelf}.
 */
@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final AdresseRepository adresseRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository,
                              AdresseRepository adresseRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.adresseRepository = adresseRepository;
    }

    /**
     * Retourne la liste complète de tous les utilisateurs enregistrés dans le système.
     * Accessible uniquement aux administrateurs.
     *
     * @return la liste de tous les utilisateurs sous forme de DTOs
     */
    public List<UtilisateurDTO> findAll() {
        return utilisateurRepository.findAll()
                .stream()
                .map(UtilisateurAdapter::toDTO)
                .toList();
    }

    /**
     * Retourne le profil d'un utilisateur identifié par son identifiant.
     * L'accès est autorisé uniquement si l'appelant est administrateur ou s'il consulte son propre profil.
     *
     * @param id l'identifiant de l'utilisateur recherché
     * @return le DTO de l'utilisateur trouvé
     * @throws org.springframework.web.server.ResponseStatusException 404 si l'utilisateur n'existe pas,
     *         403 si l'appelant n'est pas autorisé à consulter ce profil
     */
    @RequiresAdminOrSelf
    public UtilisateurDTO findById(int id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
        return UtilisateurAdapter.toDTO(utilisateur);
    }

    /**
     * Met à jour le profil d'un utilisateur (prénom, nom et adresse).
     * L'adresse est réutilisée si elle existe déjà en base, sinon elle est créée à la volée.
     * L'accès est autorisé uniquement si l'appelant est administrateur ou s'il modifie son propre profil.
     *
     * @param id      l'identifiant de l'utilisateur à modifier
     * @param request les nouvelles données du profil (prénom, nom, adresse)
     * @return le DTO mis à jour de l'utilisateur
     * @throws org.springframework.web.server.ResponseStatusException 404 si l'utilisateur n'existe pas,
     *         403 si l'appelant n'est pas autorisé à modifier ce profil
     */
    @RequiresAdminOrSelf
    @Transactional
    public UtilisateurDTO update(int id, ModifierUtilisateurRequestDTO request) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setNom(request.getNom());

        Adresse adresse = adresseRepository
                .findByVilleAndRueAndNumeroRue(
                        request.getAdresse().getVille(),
                        request.getAdresse().getRue(),
                        request.getAdresse().getNumeroRue())
                .orElseGet(() -> {
                    Adresse nouvelleAdresse = AdresseAdapter.toModel(request.getAdresse());
                    return adresseRepository.save(nouvelleAdresse);
                });
        utilisateur.setAdresse(adresse);

        return UtilisateurAdapter.toDTO(utilisateurRepository.save(utilisateur));
    }

    /**
     * Supprime définitivement le compte d'un utilisateur.
     * L'accès est autorisé uniquement si l'appelant est administrateur ou s'il supprime son propre compte.
     *
     * @param id l'identifiant de l'utilisateur à supprimer
     * @throws org.springframework.web.server.ResponseStatusException 404 si l'utilisateur n'existe pas,
     *         403 si l'appelant n'est pas autorisé à supprimer ce compte
     */
    @RequiresAdminOrSelf
    @Transactional
    public void deleteById(int id) {
        utilisateurRepository.deleteById(id);
    }
}
