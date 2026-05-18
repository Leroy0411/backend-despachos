package com.citt.controller;

import com.citt.persistence.entity.Despacho;
import com.citt.persistence.services.DespachoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/despachos")
public class DespachoController {

    @Autowired
    private DespachoService despachoService;

    @GetMapping
    public ResponseEntity<List<Despacho>> findAll() {
        return ResponseEntity.ok(despachoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Despacho> findById(@PathVariable Long id) {
        return despachoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Despacho> create(@RequestBody Despacho despacho) {
        return ResponseEntity.status(HttpStatus.CREATED).body(despachoService.save(despacho));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Despacho> update(@PathVariable Long id, @RequestBody Despacho despacho) {
        return ResponseEntity.ok(despachoService.update(id, despacho));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        despachoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
