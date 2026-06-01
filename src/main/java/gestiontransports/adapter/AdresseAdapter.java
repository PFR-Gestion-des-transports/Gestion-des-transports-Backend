package gestiontransports.adapter;

import gestiontransports.dto.adresse.AdresseDTO;
import gestiontransports.model.Adresse;

public class AdresseAdapter {

    public static AdresseDTO toDTO(Adresse adresse) {
        AdresseDTO dto = new AdresseDTO();
        dto.setId(adresse.getId());
        dto.setVille(adresse.getVille());
        dto.setRue(adresse.getRue());
        dto.setNumeroRue(adresse.getNumeroRue());
        return dto;
    }

    public static Adresse toModel(AdresseDTO dto) {
        Adresse adresse = new Adresse();
        adresse.setId(dto.getId());
        adresse.setVille(dto.getVille());
        adresse.setRue(dto.getRue());
        adresse.setNumeroRue(dto.getNumeroRue());
        return adresse;
    }
}
