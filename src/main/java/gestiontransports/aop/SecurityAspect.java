package gestiontransports.aop;

import gestiontransports.interfaces.OwnedByUtilisateur;
import gestiontransports.model.Covoiturage;
import gestiontransports.model.ReservationVehicule;
import gestiontransports.model.Utilisateur;
import gestiontransports.model.Vehicule;
import gestiontransports.repository.CovoiturageRepository;
import gestiontransports.repository.ReservationVehiculeRepository;
import gestiontransports.repository.UtilisateurRepository;
import gestiontransports.repository.VehiculeRepository;
import gestiontransports.security.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * Aspect Spring AOP gérant les vérifications d'autorisation déclarées via {@link RequiresAdminOrSelf}.
 * S'exécute avant chaque méthode annotée, charge l'entité concernée depuis la base de données,
 * et vérifie que l'utilisateur authentifié est soit administrateur, soit propriétaire de la ressource.
 */
@Aspect
@Component
public class SecurityAspect {

    private final Map<Class<?>, JpaRepository<? extends OwnedByUtilisateur, Integer>> repositoryMap;

    public SecurityAspect(VehiculeRepository vehiculeRepository,
                          CovoiturageRepository covoiturageRepository,
                          ReservationVehiculeRepository reservationVehiculeRepository,
                          UtilisateurRepository utilisateurRepository) {
        this.repositoryMap = Map.of(
                Vehicule.class,            vehiculeRepository,
                Covoiturage.class,         covoiturageRepository,
                ReservationVehicule.class, reservationVehiculeRepository,
                Utilisateur.class,         utilisateurRepository
        );
    }

    /**
     * Intercepte les méthodes annotées avec {@link RequiresAdminOrSelf}, charge l'entité
     * identifiée par le premier paramètre {@code int id} et vérifie l'autorisation.
     *
     * @param joinPoint            le point d'interception fournissant les arguments de la méthode
     * @param requiresAdminOrSelf  l'annotation portant le type d'entité à charger
     * @throws ResponseStatusException 404 si l'entité est introuvable, 403 si l'accès est interdit
     */
    @Before("@annotation(requiresAdminOrSelf)")
    public void checkAdminOrSelf(JoinPoint joinPoint, RequiresAdminOrSelf requiresAdminOrSelf) {
        int id = (int) joinPoint.getArgs()[0];
        OwnedByUtilisateur entity = findEntity(requiresAdminOrSelf.entity(), id);

        if (!SecurityUtils.isUserAuthorizedAdminAndSelf(entity.getUtilisateur())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès interdit");
        }
    }

    /**
     * Charge l'entité du type donné depuis son repository.
     * Le cast non vérifié est intentionnel et sûr : toutes les entrées de la map implémentent
     * {@link OwnedByUtilisateur} et le résultat est uniquement utilisé via cette interface.
     */
    @SuppressWarnings("unchecked")
    private OwnedByUtilisateur findEntity(Class<?> entityClass, int id) {
        JpaRepository<OwnedByUtilisateur, Integer> repository =
                (JpaRepository<OwnedByUtilisateur, Integer>) repositoryMap.get(entityClass);
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ressource introuvable"));
    }
}
