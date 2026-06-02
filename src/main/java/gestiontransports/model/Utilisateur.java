package gestiontransports.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @JsonIgnore
    @Column(name = "MotDePasse", nullable = false, length = 60)
    private String motDePasse;

    @ManyToOne
    @JoinColumn(name = "AdresseId", nullable = false,
        foreignKey = @ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (AdresseId) REFERENCES Adresse(Id) ON DELETE RESTRICT ON UPDATE CASCADE"
        ))
    private Adresse adresse;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "utilisateur_roles", joinColumns = @JoinColumn(name = "utilisateur_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

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

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}
