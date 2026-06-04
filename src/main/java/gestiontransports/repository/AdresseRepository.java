package gestiontransports.repository;

import gestiontransports.model.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository JPA pour l'entité {@link Adresse}.
 * Fournit les opérations CRUD standard ainsi qu'une recherche par combinaison unique
 * de ville, rue et numéro, utilisée pour éviter la duplication des adresses en base.
 */
public interface AdresseRepository extends JpaRepository<Adresse, Integer> {

    /**
     * Recherche une adresse correspondant exactement à la combinaison ville, rue et numéro de rue.
     * Permet de réutiliser une adresse existante plutôt que d'en créer une doublon.
     *
     * @param ville      la ville de l'adresse recherchée
     * @param rue        le nom de la rue
     * @param numeroRue  le numéro dans la rue
     * @return un {@link Optional} contenant l'adresse si elle existe, vide sinon
     */
    Optional<Adresse> findByVilleAndRueAndNumeroRue(String ville, String rue, String numeroRue);
}
