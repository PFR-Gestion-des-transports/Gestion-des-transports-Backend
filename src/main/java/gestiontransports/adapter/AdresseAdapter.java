package gestiontransports.adapter;

import gestiontransports.dto.adresse.AdresseInputDTO;
import gestiontransports.dto.adresse.AdresseOutputDTO;
import gestiontransports.model.Adresse;
import org.springframework.lang.NonNull;

public class AdresseAdapter {

    public static AdresseOutputDTO toDTO(Adresse adresse) {
        AdresseOutputDTO dto = new AdresseOutputDTO();
        dto.setId(adresse.getId());
        dto.setVille(adresse.getVille());
        dto.setRue(adresse.getRue());
        dto.setNumeroRue(adresse.getNumeroRue());
        return dto;
    }

    public static @NonNull Adresse toModel(AdresseInputDTO dto) {
        Adresse adresse = new Adresse();
        adresse.setVille(dto.getVille());
        adresse.setRue(dto.getRue());
        adresse.setNumeroRue(dto.getNumeroRue());
        return adresse;
    }
}
