package com.citt.persistence.services;

import com.citt.exceptions.DespachoNotFoundException;
import com.citt.persistence.entity.Despacho;
import com.citt.persistence.repository.DespachoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DespachoServiceImpl implements DespachoService {

    @Autowired
    private DespachoRepository despachoRepository;

    @Override
    public List<Despacho> findAll() {
        return despachoRepository.findAll();
    }

    @Override
    public Optional<Despacho> findById(Long id) {
        return despachoRepository.findById(id);
    }

    @Override
    public Despacho save(Despacho despacho) {
        return despachoRepository.save(despacho);
    }

    @Override
    public Despacho update(Long id, Despacho despacho) {
        Despacho existing = despachoRepository.findById(id)
                .orElseThrow(() -> new DespachoNotFoundException("Despacho no encontrado con id: " + id));
        existing.setNumeroDespacho(despacho.getNumeroDespacho());
        existing.setEstado(despacho.getEstado());
        existing.setDireccionDestino(despacho.getDireccionDestino());
        existing.setObservaciones(despacho.getObservaciones());
        existing.setFechaDespacho(despacho.getFechaDespacho());
        existing.setFechaCierre(despacho.getFechaCierre());
        return despachoRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        if (!despachoRepository.existsById(id)) {
            throw new DespachoNotFoundException("Despacho no encontrado con id: " + id);
        }
        despachoRepository.deleteById(id);
    }
}
