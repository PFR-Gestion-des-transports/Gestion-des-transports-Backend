package gestiontransports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Point d'entrée principal de l'application Spring Boot de gestion des transports.
 * Active la planification des tâches via {@link EnableScheduling} pour les mises à jour
 * automatiques de statuts (réservations, covoiturages, etc.).
 */
@SpringBootApplication
@EnableScheduling
public class GestionTransportsApplication {

    /**
     * Démarre l'application Spring Boot.
     *
     * @param args arguments passés en ligne de commande
     */
    public static void main(String[] args) {
        SpringApplication.run(GestionTransportsApplication.class, args);
    }
}
