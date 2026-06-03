package gestiontransports.model;

import jakarta.persistence.*;

/**
 * Entité représentant une adresse physique composée d'une ville, d'une rue et d'un numéro de rue.
 * Cette entité est partagée entre les utilisateurs (adresse personnelle) et les covoiturages
 * (adresses de départ et d'arrivée).
 */
@Entity
@Table(name = "Adresse")
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Ville", nullable = false, length = 100)
    private String ville;

    @Column(name = "Rue", nullable = false, length = 150)
    private String rue;

    @Column(name = "NumeroRue", nullable = false, length = 10)
    private String numeroRue;

    public Adresse() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getRue() { return rue; }
    public void setRue(String rue) { this.rue = rue; }

    public String getNumeroRue() { return numeroRue; }
    public void setNumeroRue(String numeroRue) { this.numeroRue = numeroRue; }
}
