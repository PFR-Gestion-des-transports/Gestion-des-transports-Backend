# Design — Initialisation Spring Boot Backend

**Date:** 2026-06-01  
**Projet:** Gestion des transports  
**Groupe:** groupe3 / Diginamic

---

## Contexte

Initialisation d'un backend Spring Boot pour le projet scolaire de gestion des transports. Seule la partie back-end est concernée pour l'instant. Le projet suit une architecture MVC en couches séparées.

---

## Architecture

Architecture en couches classique (package par couche) :

| Couche | Package | Rôle |
|---|---|---|
| Controller | `controller/` | Exposition des routes HTTP REST |
| Service | `service/` | Logique métier |
| Repository | `repository/` | Accès aux données via JPA |
| Model | `model/` | Entités JPA mappées sur la DB |

---

## Structure des fichiers

```
src/
└── main/
    ├── java/
    │   └── gestiontransports/
    │       ├── controller/
    │       ├── service/
    │       ├── repository/
    │       ├── model/
    │       └── GestionTransportsApplication.java
    └── resources/
        └── application.properties
```

---

## Stack technique

- **Java 21**
- **Spring Boot 3.x**
- **Maven** (gestion des dépendances)
- **MySQL** (base de données distante)
- **Spring Data JPA / Hibernate** (ORM)

---

## Dépendances Maven

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

---

## Configuration DB

```properties
spring.datasource.url=jdbc:mysql://82.165.152.149:3307/2025-D11-groupe3
spring.datasource.username=pfrgestran
spring.datasource.password=td89_dcx65=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## .gitignore

Ajout de `.vs/` (Visual Studio) aux exclusions, ainsi que les classiques Maven/Java :
- `target/`
- `*.class`
- `.vs/`
- Fichiers IDE (`.idea/`, `*.iml`)

---

## Ce qui n'est PAS dans ce design

- Entités métier (pas encore définies)
- Sécurité / authentification
- Tests unitaires
- Documentation API (Swagger)

Ces éléments seront ajoutés dans des specs séparées.
