package gestiontransports.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import gestiontransports.service.ReservationCovoiturageService;
import gestiontransports.dto.reservation.ReservationCovoiturageDTO;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import gestiontransports.dto.reservation.AnnulerReservationCovoiturageDTO;
import gestiontransports.dto.reservation.CreerReservationCovoiturageDTO;;

@RestController
@RequestMapping("/reservations-covoiturage")
public class ReservationCovoiturageController {
    
    private final ReservationCovoiturageService reservationCovoiturageService;

    public ReservationCovoiturageController(ReservationCovoiturageService reservationCovoiturageService) {
        this.reservationCovoiturageService = reservationCovoiturageService;
    }

    @GetMapping("/findall")
    public ResponseEntity<List<ReservationCovoiturageDTO>> findAll() {
        return ResponseEntity.ok(reservationCovoiturageService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationCovoiturageDTO> findById(@PathVariable int id) {
        return ResponseEntity.ok(reservationCovoiturageService.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ReservationCovoiturageDTO> create(@RequestBody CreerReservationCovoiturageDTO request) {
        return ResponseEntity.status(201).body(reservationCovoiturageService.create(request));
    }

    @PutMapping("/annuler")
    public ResponseEntity<ReservationCovoiturageDTO> annuler(@RequestBody AnnulerReservationCovoiturageDTO request) {
        return ResponseEntity.ok(reservationCovoiturageService.annuler(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        reservationCovoiturageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
