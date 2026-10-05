//Juan Camilo Guerrero Diaz
package com.mundial.controller;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mundial.model.Equipo;
import com.mundial.model.Mundial;
import com.mundial.repository.EquipoRepository;
import com.mundial.repository.MundialRepository;

@RestController
@RequestMapping("/api")
public class MundialRestController {

    private final MundialRepository mundialRepository;
    private final EquipoRepository equipoRepository;

    public MundialRestController(MundialRepository mundialRepository, EquipoRepository equipoRepository) {
        this.mundialRepository = mundialRepository;
        this.equipoRepository = equipoRepository;
    }

    // ===================== EQUIPOS =====================

    @GetMapping("/equipos")
    public List<Equipo> listarEquipos() {
        return equipoRepository.findAll();
    }

    @GetMapping("/equipos/{id}")
    public ResponseEntity<Equipo> obtenerEquipo(@PathVariable String id) {
        return equipoRepository.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/equipos")
    public ResponseEntity<Equipo> crearEquipo(@RequestBody Equipo equipo) {
        equipo.setId(UUID.randomUUID().toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(equipoRepository.save(equipo));
    }

    @PutMapping("/equipos/{id}")
    public ResponseEntity<Equipo> actualizarEquipo(@PathVariable String id, @RequestBody Equipo equipo) {
        if (!equipoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        equipo.setId(id);
        return ResponseEntity.ok(equipoRepository.save(equipo));
    }

    @DeleteMapping("/equipos/{id}")
    public ResponseEntity<Void> eliminarEquipo(@PathVariable String id) {
        if (!equipoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        equipoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ===================== MUNDIALES =====================

    @GetMapping("/mundiales")
    public List<Mundial> listarMundiales() {
        return mundialRepository.findAll();
    }

    @GetMapping("/mundiales/{id}")
    public ResponseEntity<Mundial> obtenerMundial(@PathVariable String id) {
        return mundialRepository.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/mundiales")
    public ResponseEntity<Mundial> crearMundial(@RequestBody Mundial mundial) {
        mundial.setId(UUID.randomUUID().toString());
        resolverReferencias(mundial);
        return ResponseEntity.status(HttpStatus.CREATED).body(mundialRepository.save(mundial));
    }

    @PutMapping("/mundiales/{id}")
    public ResponseEntity<Mundial> actualizarMundial(@PathVariable String id, @RequestBody Mundial mundial) {
        if (!mundialRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        mundial.setId(id);
        resolverReferencias(mundial);
        return ResponseEntity.ok(mundialRepository.save(mundial));
    }

    @DeleteMapping("/mundiales/{id}")
    public ResponseEntity<Void> eliminarMundial(@PathVariable String id) {
        if (!mundialRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        mundialRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /** Sustituye los {"id": "..."} recibidos en el JSON por los equipos reales guardados en MongoDB. */
    private void resolverReferencias(Mundial mundial) {
        if (mundial.getEquipos() != null) {
            List<String> ids = mundial.getEquipos().stream()
                    .filter(Objects::nonNull)
                    .map(Equipo::getId)
                    .filter(Objects::nonNull)
                    .toList();
            mundial.setEquipos(equipoRepository.findAllById(ids));
        }
        Equipo campeon = mundial.getCampeon();
        mundial.setCampeon(campeon != null && campeon.getId() != null
                ? equipoRepository.findById(campeon.getId()).orElse(null)
                : null);
    }
}
