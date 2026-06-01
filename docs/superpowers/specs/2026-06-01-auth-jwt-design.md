# Design — Authentification JWT Spring Security

**Date:** 2026-06-01
**Projet:** Gestion des transports
**Groupe:** groupe3 / Diginamic

---

## Contexte

Mise en place d'un système d'authentification pour le backend Spring Boot. Trois routes sont exposées : création de compte, connexion, déconnexion. L'authentification repose sur JWT Bearer token. Les rôles sont gérés nativement via Spring Security.

---

## Rôles

Deux rôles possibles, stockés comme enum Java :
- `COLLABORATEUR`
- `ADMINISTRATEUR`

Un utilisateur peut avoir les deux simultanément.

Stockage via `@ElementCollection` sur `Utilisateur` — Hibernate crée automatiquement une table `utilisateur_roles`.

---

## Routes

| Méthode | URL | Auth | Description |
|---|---|---|---|
| POST | `/auth/creer-compte` | Publique | Créer un compte, retourne un JWT |
| POST | `/auth/se-connecter` | Publique | Connexion, retourne un JWT |
| POST | `/auth/se-deconnecter` | Bearer requis | Retourne 200 (logout client-side) |

### POST /auth/creer-compte

**Request body :**
```json
{
  "prenom": "Jean",
  "nom": "Dupont",
  "email": "jean@mail.com",
  "motDePasse": "monMotDePasse",
  "adresseId": 1,
  "roles": ["COLLABORATEUR"]
}
```

**Response 201 :**
```json
{ "token": "eyJhbGci..." }
```

**Flux :**
1. Vérifier que l'email n'est pas déjà utilisé → `409 Conflict` sinon
2. Hasher le mot de passe avec BCrypt
3. Sauvegarder l'utilisateur avec ses rôles
4. Générer un JWT
5. Retourner le token

---

### POST /auth/se-connecter

**Request body :**
```json
{
  "email": "jean@mail.com",
  "motDePasse": "monMotDePasse"
}
```

**Response 200 :**
```json
{ "token": "eyJhbGci..." }
```

**Flux :**
1. Charger l'utilisateur par email → `401 Unauthorized` si introuvable
2. Vérifier le mot de passe avec BCrypt → `401 Unauthorized` si incorrect
3. Générer un JWT
4. Retourner le token

---

### POST /auth/se-deconnecter

**Headers requis :** `Authorization: Bearer <token>`

**Response 200 :**
```json
{ "message": "Déconnexion réussie" }
```

**Flux :** Spring Security valide le token → le serveur retourne 200 → le client supprime son token localement.

---

## Token JWT

**Contenu du payload :**
```json
{
  "sub": "jean@mail.com",
  "roles": ["COLLABORATEUR"],
  "iat": 1234567890,
  "exp": 1234571490
}
```

**Expiration :** 1 heure.

**Algorithme :** HS256 avec une clé secrète configurée dans `application.properties` :

```properties
jwt.secret=<chaîne Base64 d'au moins 256 bits>
jwt.expiration=3600000
```

---

## Composants

| Fichier | Package | Rôle |
|---|---|---|
| `Role.java` | `model` | Enum COLLABORATEUR / ADMINISTRATEUR |
| `Utilisateur.java` | `model` | Ajout `Set<Role> roles` + `VARCHAR(60) motDePasse` |
| `UtilisateurRepository.java` | `repository` | `findByEmail(String email)` |
| `JwtUtil.java` | `security` | Générer / valider / lire les claims d'un JWT |
| `JwtFilter.java` | `security` | `OncePerRequestFilter` — injecte l'auth dans le contexte |
| `UserDetailsServiceImpl.java` | `security` | Implémente `UserDetailsService`, charge par email |
| `SecurityConfig.java` | `security` | Config Spring Security stateless + routes publiques |
| `CreerCompteRequest.java` | `dto` | Corps requête inscription |
| `ConnexionRequest.java` | `dto` | Corps requête connexion |
| `ConnexionResponse.java` | `dto` | Réponse avec token |
| `AuthService.java` | `service` | Logique métier inscription + connexion |
| `AuthController.java` | `controller` | 3 routes REST |

---

## Spring Security

- **Session :** `STATELESS` — aucune session HTTP
- **CSRF :** désactivé (inutile en REST stateless)
- **Routes publiques :** `/auth/creer-compte`, `/auth/se-connecter`
- **Routes protégées :** tout le reste (Bearer token requis)
- **Erreurs automatiques :**
  - Token absent ou malformé → `401 Unauthorized`
  - Token valide, accès refusé → `403 Forbidden`

---

## Dépendances à ajouter dans pom.xml

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
```

---

## Modification de la DB

`MotDePasse` passe de `CHAR(64)` à `VARCHAR(60)` pour correspondre exactement à la longueur d'un hash BCrypt.

Script de migration :
```sql
ALTER TABLE Utilisateur MODIFY COLUMN MotDePasse VARCHAR(60) NOT NULL;
```

---

## Ce qui n'est PAS dans ce design

- Refresh token
- Route pour modifier le mot de passe
- Routes protégées par rôle (ex: admin seulement)
- Tests unitaires de sécurité

Ces éléments seront ajoutés dans des specs séparées si besoin.
