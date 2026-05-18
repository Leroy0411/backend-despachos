package com.citt.persistence.services;

import com.citt.persistence.entity.Despacho;

import java.util.List;
import java.util.Optional;

public interface DespachoService {
    List<Despacho> findAll();
    Optional<Despacho> findById(Long id);
    Despacho save(Despacho despacho);
    Despacho update(Long id, Despacho despacho);
    void deleteById(Long id);
}
