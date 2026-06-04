package gestiontransports.interfaces;

import gestiontransports.model.Utilisateur;

/**
 * Interface marquant les entités possédées par un {@link Utilisateur}.
 * Permet à l'aspect de sécurité {@code SecurityAspect} de résoudre le propriétaire
 * de n'importe quelle entité de façon générique, sans connaître son type concret.
 */
public interface OwnedByUtilisateur {

    /**
     * Retourne le propriétaire de cette entité.
     * Pour {@link Utilisateur} lui-même, retourne {@code this}.
     *
     * @return l'utilisateur propriétaire de l'entité
     */
    Utilisateur getUtilisateur();
}
