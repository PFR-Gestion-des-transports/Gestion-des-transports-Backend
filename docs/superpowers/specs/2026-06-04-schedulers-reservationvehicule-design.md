# Design — Schedulers de transition de statut pour ReservationVehicule

## Contexte

Les réservations de véhicules (`ReservationVehicule`) ont un cycle de vie à 4 statuts :
`PAS_COMMENCEE → COMMENCEE → TERMINEE` (ou `ANNULEE` par action manuelle).

Aujourd'hui ces transitions ne se produisent jamais automatiquement — un statut `PAS_COMMENCEE`
reste figé même après la date de début. Le but est d'automatiser les deux transitions temporelles
via des schedulers Spring Boot.

**Périmètre :** `ReservationVehicule` uniquement. `Covoiturage` est exclu (pas de `dateHeureFin`,
sera traité dans une passe ultérieure avec calcul de temps de trajet ou action manuelle).

## Transitions à automatiser

| Transition | Condition |
|---|---|
| `PAS_COMMENCEE → COMMENCEE` | `dateHeureDebut <= maintenant` |
| `COMMENCEE → TERMINEE` | `dateHeureFin <= maintenant` |

Les statuts `ANNULEE` ne sont jamais touchés par le scheduler.

## Architecture

### Emplacement du code

Les méthodes `@Scheduled` sont ajoutées directement dans `ReservationVehiculeService`,
en cohérence avec le pattern existant de `TokenBlacklistService`.

`@EnableScheduling` est déjà déclaré sur `GestionTransportsApplication`.

### Repository — `ReservationVehiculeRepository`

Deux nouvelles méthodes bulk UPDATE (aucun fetch en mémoire) :

```java
@Modifying
@Query("UPDATE ReservationVehicule r SET r.statutReservation = 'COMMENCEE' " +
       "WHERE r.statutReservation = 'PAS_COMMENCEE' AND r.dateHeureDebut <= :now")
int demarrerReservationsEchues(@Param("now") LocalDateTime now);

@Modifying
@Query("UPDATE ReservationVehicule r SET r.statutReservation = 'TERMINEE' " +
       "WHERE r.statutReservation = 'COMMENCEE' AND r.dateHeureFin <= :now")
int terminerReservationsEchues(@Param("now") LocalDateTime now);
```

### Service — `ReservationVehiculeService`

Deux méthodes `@Scheduled`, chacune dans une transaction indépendante :

```java
@Scheduled(cron = "0 0/30 * * * *")
@Transactional
public void demarrerReservationsEchues() {
    reservationVehiculeRepository.demarrerReservationsEchues(LocalDateTime.now());
}

@Scheduled(cron = "0 0/30 * * * *")
@Transactional
public void terminerReservationsEchues() {
    reservationVehiculeRepository.terminerReservationsEchues(LocalDateTime.now());
}
```

**Cadence :** toutes les 30 minutes (`0 0/30 * * * *`).

**Deux méthodes séparées** (et non une seule) : si `terminerReservationsEchues` échoue,
`demarrerReservationsEchues` est déjà committée — pas de rollback de la première transition.

## Ce qui est exclu

- Index BDD : pas nécessaires au volume actuel, à reconsidérer si les perfs se dégradent.
- `Covoiturage` : pas de `dateHeureFin`, traitement différé.
- `StatutVehicule` : transitions manuelles uniquement, pas de dates associées.
