package gestiontransports.aop;

import gestiontransports.interfaces.OwnedByUtilisateur;
import gestiontransports.model.Utilisateur;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation de sécurité déclarative indiquant que la méthode est réservée
 * à l'administrateur ou au propriétaire de la ressource identifiée par le premier
 * paramètre {@code int id} de la méthode.
 *
 * <p>L'aspect {@link SecurityAspect} intercepte les méthodes annotées, charge l'entité
 * correspondante via son repository, et vérifie que l'utilisateur authentifié est
 * soit administrateur, soit propriétaire de la ressource.</p>
 *
 * <p>Exemple d'utilisation :</p>
 * <pre>
 * {@code @RequiresAdminOrSelf(entity = Vehicule.class)
 * public VehiculeDTO findById(int id) { ... }
 *
 * @RequiresAdminOrSelf
 * public UtilisateurDTO findById(int id) { ... }}
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresAdminOrSelf {

    /**
     * Type de l'entité à charger pour la vérification d'autorisation.
     * Doit implémenter {@link OwnedByUtilisateur}.
     * Par défaut {@link Utilisateur} pour les routes {@code /utilisateurs/{id}}.
     */
    Class<? extends OwnedByUtilisateur> entity() default Utilisateur.class;
}
