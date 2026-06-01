package gestiontransports.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;

@Entity
@Table(name = "Covoiturage")
public class Covoiturage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nbrPlaceInitial", nullable = false, length = 100)
    private String nbrPlaceInitial;

    @Column(name = "nbrPlaceRestante", nullable = false, length = 150)
    private String nbrPlaceRestante;

    @Column(name = "dateHeureDebut", nullable = false, length = 10)
    private String dateHeureDebut;

    @ManyToOne
    @JoinColumn(name = "AdresseIdDepart", nullable = false,
        foreignKey = @ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (AdresseIdDepart) REFERENCES Adresse(Id) ON DELETE RESTRICT ON UPDATE CASCADE"
        ))
    private Adresse adresseDepart;

    @ManyToOne
    @JoinColumn(name = "AdresseIdArrivee", nullable = false,
        foreignKey = @ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (AdresseIdArrivee) REFERENCES Adresse(Id) ON DELETE RESTRICT ON UPDATE CASCADE"
        ))
    private Adresse adresseArrivee;
}
