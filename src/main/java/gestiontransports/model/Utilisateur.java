package gestiontransports.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Prenom", nullable = false, length = 50)
    private String prenom;

    @Column(name = "Nom", nullable = false, length = 50)
    private String nom;

    @Column(name = "Email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "MotDePasse", nullable = false, columnDefinition = "CHAR(64)")
    private String motDePasse;

    @ManyToOne
    @JoinColumn(name = "AdresseId", nullable = false,
        foreignKey = @ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (AdresseId) REFERENCES Adresse(Id) ON DELETE RESTRICT ON UPDATE CASCADE"
        ))
    private Adresse adresse;

    public Utilisateur() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public Adresse getAdresse() { return adresse; }
    public void setAdresse(Adresse adresse) { this.adresse = adresse; }
}
