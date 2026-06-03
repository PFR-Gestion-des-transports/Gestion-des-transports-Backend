package gestiontransports.dto.adresse;

import jakarta.validation.constraints.NotBlank;

public class AdresseDTO {

    private int id;

    @NotBlank
    private String ville;

    @NotBlank
    private String rue;

    @NotBlank
    private String numeroRue;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getRue() { return rue; }
    public void setRue(String rue) { this.rue = rue; }

    public String getNumeroRue() { return numeroRue; }
    public void setNumeroRue(String numeroRue) { this.numeroRue = numeroRue; }
}
