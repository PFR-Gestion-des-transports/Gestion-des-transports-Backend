package gestiontransports.dto.adresse;

public class AdresseOutputDTO {

    private int id;
    private String ville;
    private String rue;
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
