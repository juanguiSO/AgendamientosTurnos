package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.dto.MisionNumeroDTO;
import com.agendamientos.agendamientosTurnos.entity.MisionXViaje;
import com.agendamientos.agendamientosTurnos.service.MisionXViajeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/misionXViaje")
public class MisionXViajeController {
    private final MisionXViajeService service;

    public MisionXViajeController(MisionXViajeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MisionXViaje>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MisionXViaje> getById(@PathVariable Long id) {
        Optional<MisionXViaje> misionXViaje = service.findById(id);
        return misionXViaje.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MisionXViaje> create(@RequestBody MisionXViaje misionXViaje) {
        return ResponseEntity.ok(service.save(misionXViaje));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MisionXViaje> update(@PathVariable Long id, @RequestBody MisionXViaje misionXViaje) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        misionXViaje.setId(id);
        return ResponseEntity.ok(service.save(misionXViaje));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }


  /**@GetMapping("/viaje/{idViaje}/misiones")
  public ResponseEntity<List<MisionNumeroDTO>> getMisionesByViaje(@PathVariable Integer idViaje) {
      List<MisionNumeroDTO> misiones = service.findByViajeId(idViaje)
              .stream()
              .map(misionXViaje -> new MisionNumeroDTO(misionXViaje.getMision().getNumeroMision()))
              .toList();
      return misiones.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(misiones);
  }*/

  @GetMapping("/viaje/{idViaje}/misiones")
  public ResponseEntity<List<MisionNumeroDTO>> getMisionesByViaje(@PathVariable Integer idViaje) {

      List<MisionNumeroDTO> misiones = service.findMisionNumeroDTOsByViajeId(idViaje);

      return misiones.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(misiones);
  }


}