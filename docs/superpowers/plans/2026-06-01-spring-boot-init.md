# Spring Boot Backend Initialization — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Initialiser la structure de base d'un projet Spring Boot avec architecture Controller/Service/Repository connecté à une base MySQL distante.

**Architecture:** Projet Maven Spring Boot 3.3.x. Structure en couches : controller, service, repository, model. Package racine `gestiontransports`. Connexion MySQL via Spring Data JPA/Hibernate.

**Tech Stack:** Java 21, Spring Boot 3.3.x, Spring Data JPA, MySQL Connector/J 8.x, Maven 3.x

---

## Carte des fichiers

| Fichier | Action | Rôle |
|---|---|---|
| `.gitignore` | Créer | Exclure .vs/, target/, .class, IDE |
| `pom.xml` | Créer | Config Maven + dépendances Spring Boot |
| `src/main/java/gestiontransports/GestionTransportsApplication.java` | Créer | Point d'entrée Spring Boot |
| `src/main/resources/application.properties` | Créer | Config DB MySQL |
| `src/main/java/gestiontransports/controller/.gitkeep` | Créer | Marquer le package vide dans git |
| `src/main/java/gestiontransports/service/.gitkeep` | Créer | Marquer le package vide dans git |
| `src/main/java/gestiontransports/repository/.gitkeep` | Créer | Marquer le package vide dans git |
| `src/main/java/gestiontransports/model/.gitkeep` | Marquer le package vide dans git |
| `src/test/java/gestiontransports/GestionTransportsApplicationTests.java` | Créer | Test de chargement du contexte Spring |

---

## Task 1 : .gitignore

**Files:**
- Create: `.gitignore`

- [ ] **Step 1 : Créer le fichier `.gitignore`**

```
# Visual Studio
.vs/

# Maven
target/
*.jar
*.war
*.class

# IDE
.idea/
*.iml
.vscode/
*.iws
*.ipr

# OS
.DS_Store
Thumbs.db

# Logs
*.log
```

---

## Task 2 : pom.xml

**Files:**
- Create: `pom.xml`

- [ ] **Step 1 : Créer le `pom.xml`**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.5</version>
        <relativePath/>
    </parent>

    <groupId>groupe3.diginamic</groupId>
    <artifactId>gestiontransports</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>gestiontransports</name>
    <description>Gestion des transports — Backend Spring Boot</description>

    <properties>
        <java.version>21</java.version>
    </properties>

    <dependencies>
        <!-- API REST -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- JPA / Hibernate -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- Driver MySQL -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Tests -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## Task 3 : Classe principale

**Files:**
- Create: `src/main/java/gestiontransports/GestionTransportsApplication.java`

- [ ] **Step 1 : Créer la classe principale**

```java
package gestiontransports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionTransportsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionTransportsApplication.class, args);
    }
}
```

---

## Task 4 : Configuration DB

**Files:**
- Create: `src/main/resources/application.properties`

- [ ] **Step 1 : Créer `application.properties`**

```properties
# Datasource MySQL
spring.datasource.url=jdbc:mysql://82.165.152.149:3307/2025-D11-groupe3?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=pfrgestran
spring.datasource.password=td89_dcx65=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

# Port serveur
server.port=8080
```

---

## Task 5 : Structure des packages

**Files:**
- Create: `src/main/java/gestiontransports/controller/.gitkeep`
- Create: `src/main/java/gestiontransports/service/.gitkeep`
- Create: `src/main/java/gestiontransports/repository/.gitkeep`
- Create: `src/main/java/gestiontransports/model/.gitkeep`

- [ ] **Step 1 : Créer les dossiers avec des fichiers `.gitkeep`**

Créer un fichier `.gitkeep` vide dans chacun de ces dossiers pour que git les tracke :
- `src/main/java/gestiontransports/controller/.gitkeep`
- `src/main/java/gestiontransports/service/.gitkeep`
- `src/main/java/gestiontransports/repository/.gitkeep`
- `src/main/java/gestiontransports/model/.gitkeep`

---

## Task 6 : Test de chargement du contexte

**Files:**
- Create: `src/test/java/gestiontransports/GestionTransportsApplicationTests.java`

- [ ] **Step 1 : Créer le test**

```java
package gestiontransports;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GestionTransportsApplicationTests {

    @Test
    void contextLoads() {
    }
}
```

- [ ] **Step 2 : Lancer les tests**

```bash
mvn test
```

Résultat attendu :
```
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Si le test échoue avec une erreur de connexion DB : vérifier que la DB MySQL est accessible depuis le réseau (ping `82.165.152.149`).

- [ ] **Step 3 : Lancer l'application**

```bash
mvn spring-boot:run
```

Résultat attendu : serveur démarré sur `http://localhost:8080`

---

## Ordre d'exécution

```
Task 1 (.gitignore)
    → Task 2 (pom.xml)
        → Task 3 (main class)
        → Task 4 (application.properties)
        → Task 5 (packages)
            → Task 6 (test + run)
```
