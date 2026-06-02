package gestiontransports.dto.covoiturage;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.NotBlank;

import gestiontransports.dto.adresse.AdresseDTO;
import gestiontransports.dto.reservation.ReservationCovoiturageDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import jakarta.validation.constraints.NotEmpty;


public class CovoiturageDTO {

    private Integer id;

    @NotNull
    private Integer nbrPlaceInitial;

    @NotNull
    private Integer nbrPlaceRestante;

    @NotNull
    private LocalDateTime dateHeureDebut;

    @NotNull
    @Valid
    private AdresseDTO adresseDepart;

    @NotNull
    @Valid
    private AdresseDTO adresseArrivee;

    @NotEmpty
    private Set<ReservationCovoiturageDTO> reservations;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getNbrPlaceInitial() { return nbrPlaceInitial; }
    public void setNbrPlaceInitial(Integer nbrPlaceInitial) { this.nbrPlaceInitial = nbrPlaceInitial; }

    public Integer getNbrPlaceRestante() { return nbrPlaceRestante; }
    public void setNbrPlaceRestante(Integer nbrPlaceRestante) { this.nbrPlaceRestante = nbrPlaceRestante; }

    public LocalDateTime getDateHeureDebut() { return dateHeureDebut; }
    public void setDateHeureDebut(LocalDateTime dateHeureDebut) { this.dateHeureDebut = dateHeureDebut; }

    public AdresseDTO getAdresseDepart() { return adresseDepart; }
    public void setAdresseDepart(AdresseDTO adresseDepart) { this.adresseDepart = adresseDepart; }

    public AdresseDTO getAdresseArrivee() { return adresseArrivee; }
    public void setAdresseArrivee(AdresseDTO adresseArrivee) { this.adresseArrivee = adresseArrivee; }

    public Set<ReservationCovoiturageDTO> getReservations() { return reservations; }
    public void setReservations(Set<ReservationCovoiturageDTO> reservations) { this.reservations = reservations; }
}
