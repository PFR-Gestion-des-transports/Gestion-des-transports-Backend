package gestiontransports.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ForeignKey;

@Entity
@Table(name = "ReservationCovoiturage")
public class ReservationCovoiturage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "covoiturage", nullable = false,
        foreignKey = @ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (covoiturage) REFERENCES Covoiturage(id) ON DELETE RESTRICT ON UPDATE CASCADE"
        ))
        private Covoiturage covoiturage;
    
    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false,
        foreignKey = @ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (utilisateur_id) REFERENCES Utilisateur(id) ON DELETE RESTRICT ON UPDATE CASCADE"
        ))
        private Utilisateur utilisateur;


}
