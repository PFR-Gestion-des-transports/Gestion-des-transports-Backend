# Schedulers ReservationVehicule — Plan d'implémentation

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Automatiser les transitions de statut `PAS_COMMENCEE → COMMENCEE` et `COMMENCEE → TERMINEE` pour `ReservationVehicule` via des schedulers Spring Boot toutes les 30 minutes.

**Architecture:** Deux méthodes `@Modifying @Query` bulk UPDATE dans `ReservationVehiculeRepository` (aucun fetch en mémoire), appelées depuis deux méthodes `@Scheduled @Transactional` dans `ReservationVehiculeService` (pattern identique à `TokenBlacklistService`). `@EnableScheduling` est déjà actif sur `GestionTransportsApplication`.

**Tech Stack:** Spring Boot 3.3.5, Spring Data JPA, JUnit 5, Mockito (via `spring-boot-starter-test`)

---

## Fichiers modifiés

| Fichier | Action |
|---|---|
| `src/main/java/gestiontransports/repository/ReservationVehiculeRepository.java` | Modifier — ajouter 2 méthodes `@Modifying @Query` |
| `src/main/java/gestiontransports/service/ReservationVehiculeService.java` | Modifier — ajouter 2 méthodes `@Scheduled`, import `@Scheduled` |
| `src/test/java/gestiontransports/service/ReservationVehiculeSchedulerTest.java` | Créer — tests unitaires Mockito |

---

## Task 1 : Écrire les tests unitaires (TDD)

**Files:**
- Create: `src/test/java/gestiontransports/service/ReservationVehiculeSchedulerTest.java`

- [ ] **Step 1 : Créer le fichier de test**

```java
package gestiontransports.service;

import gestiontransports.repository.ReservationVehiculeRepository;
import gestiontransports.repository.VehiculeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservationVehiculeSchedulerTest {

    @Mock
    private ReservationVehiculeRepository reservationVehiculeRepository;

    @Mock
    private VehiculeRepository vehiculeRepository;

    @Mock
    private UtilisateurContextService utilisateurContextService;

    @InjectMocks
    private ReservationVehiculeService service;

    @Test
    void demarrerReservationsEchues_appelleRepositoryAvecDateActuelle() {
        service.demarrerReservationsEchues();
        verify(reservationVehiculeRepository).demarrerReservationsEchues(any(LocalDateTime.class));
    }

    @Test
    void terminerReservationsEchues_appelleRepositoryAvecDateActuelle() {
        service.terminerReservationsEchues();
        verify(reservationVehiculeRepository).terminerReservationsEchues(any(LocalDateTime.class));
    }
}
```

- [ ] **Step 2 : Vérifier que les tests échouent (méthodes inexistantes)**

```
mvn test -Dtest=ReservationVehiculeSchedulerTest -q
```

Résultat attendu : **ERREUR DE COMPILATION** — `demarrerReservationsEchues` et `terminerReservationsEchues` n'existent pas encore sur le repository ni le service.

---

## Task 2 : Repository — méthodes bulk UPDATE

**Files:**
- Modify: `src/main/java/gestiontransports/repository/ReservationVehiculeRepository.java`

L'état actuel du fichier se termine par la méthode `findByUtilisateurAndStatutReservationIn`.

- [ ] **Step 3 : Ajouter les imports manquants en haut du repository**

Ajouter dans les imports (après les imports existants) :

```java
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
```

- [ ] **Step 4 : Ajouter les deux méthodes bulk UPDATE dans l'interface**

Ajouter avant la fermeture `}` de l'interface :

```java
/**
 * Passe toutes les réservations dont le statut est {@code PAS_COMMENCEE} et dont la date de
 * début est passée au statut {@code COMMENCEE}. Destinée à être appelée par un scheduler.
 *
 * @param now la date et heure de référence
 * @return le nombre de réservations mises à jour
 */
@Modifying
@Transactional
@Query("UPDATE ReservationVehicule r SET r.statutReservation = gestiontransports.enums.StatutReservation.COMMENCEE " +
       "WHERE r.statutReservation = gestiontransports.enums.StatutReservation.PAS_COMMENCEE " +
       "AND r.dateHeureDebut <= :now")
int demarrerReservationsEchues(@Param("now") LocalDateTime now);

/**
 * Passe toutes les réservations dont le statut est {@code COMMENCEE} et dont la date de
 * fin est passée au statut {@code TERMINEE}. Destinée à être appelée par un scheduler.
 *
 * @param now la date et heure de référence
 * @return le nombre de réservations mises à jour
 */
@Modifying
@Transactional
@Query("UPDATE ReservationVehicule r SET r.statutReservation = gestiontransports.enums.StatutReservation.TERMINEE " +
       "WHERE r.statutReservation = gestiontransports.enums.StatutReservation.COMMENCEE " +
       "AND r.dateHeureFin <= :now")
int terminerReservationsEchues(@Param("now") LocalDateTime now);
```

Ajouter aussi `@Param` aux imports :

```java
import org.springframework.data.repository.query.Param;
```

---

## Task 3 : Service — méthodes @Scheduled

**Files:**
- Modify: `src/main/java/gestiontransports/service/ReservationVehiculeService.java`

- [ ] **Step 5 : Ajouter l'import `@Scheduled` dans le service**

Ajouter parmi les imports existants :

```java
import org.springframework.scheduling.annotation.Scheduled;
```

- [ ] **Step 6 : Ajouter les deux méthodes schedulées à la fin du service**

Ajouter avant la fermeture `}` de la classe `ReservationVehiculeService` :

```java
/**
 * Tâche planifiée exécutée toutes les 30 minutes qui passe au statut {@code COMMENCEE}
 * toutes les réservations dont la date de début est échue et qui sont encore {@code PAS_COMMENCEE}.
 */
@Scheduled(cron = "0 0/30 * * * *")
@Transactional
public void demarrerReservationsEchues() {
    reservationVehiculeRepository.demarrerReservationsEchues(LocalDateTime.now());
}

/**
 * Tâche planifiée exécutée toutes les 30 minutes qui passe au statut {@code TERMINEE}
 * toutes les réservations dont la date de fin est échue et qui sont encore {@code COMMENCEE}.
 */
@Scheduled(cron = "0 0/30 * * * *")
@Transactional
public void terminerReservationsEchues() {
    reservationVehiculeRepository.terminerReservationsEchues(LocalDateTime.now());
}
```

---

## Task 4 : Vérification

- [ ] **Step 7 : Lancer les tests unitaires — ils doivent passer**

```
mvn test -Dtest=ReservationVehiculeSchedulerTest -q
```

Résultat attendu :

```
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

- [ ] **Step 8 : Compiler le projet complet**

```
mvn compile -q
```

Résultat attendu : `BUILD SUCCESS` sans erreurs.

- [ ] **Step 9 : Commit**

```
git add src/main/java/gestiontransports/repository/ReservationVehiculeRepository.java
git add src/main/java/gestiontransports/service/ReservationVehiculeService.java
git add src/test/java/gestiontransports/service/ReservationVehiculeSchedulerTest.java
git commit -m "feat: scheduler de transition automatique des statuts ReservationVehicule (30 min)"
```
