package gestiontransports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GestionTransportsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionTransportsApplication.class, args);
    }
}
