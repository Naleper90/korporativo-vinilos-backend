package com.korporativo.korporativo_backend.service;

import com.korporativo.korporativo_backend.dto.PresupuestoDTO;
import com.korporativo.korporativo_backend.model.Cliente;
import com.korporativo.korporativo_backend.model.Presupuesto;
import com.korporativo.korporativo_backend.repository.ClienteRepository;
import com.korporativo.korporativo_backend.repository.PresupuestoRepository;
import com.korporativo.korporativo_backend.repository.ViniloConfigRepository;
import com.korporativo.korporativo_backend.exception.RecursoNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PresupuestoService {

    @Autowired
    private PresupuestoRepository presupuestoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ViniloConfigRepository viniloConfigRepository;

    public List<Presupuesto> getAllPresupuestos() {
        return presupuestoRepository.findAll();
    }

    public Presupuesto getPresupuestoById(Long id) {
        return presupuestoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Presupuesto no encontrado: " + id)
                );
    }

    public Presupuesto savePresupuesto(Presupuesto presupuesto) {
        if (presupuesto.getCliente() == null || presupuesto.getCliente().getId() == null) {
            throw new RuntimeException("Cliente requerido");
        }
        return presupuestoRepository.save(presupuesto);
    }

    public void deletePresupuesto(Long id) {
        presupuestoRepository.deleteById(id);
    }

    public List<Presupuesto> getPresupuestosByClienteId(Long clienteId) {
        return presupuestoRepository.findByClienteId(clienteId);
    }

    // -------- agregaciones --------

    public Double getTotalPresupuestosByClienteId(Long clienteId) {
        return presupuestoRepository.sumPrecioByClienteId(clienteId);
    }

    public Long countVinilosByPresupuestoId(Long presupuestoId) {
        return viniloConfigRepository.countByPresupuestoId(presupuestoId);
    }

    // -------- DTOs --------

    public PresupuestoDTO toDTO(Presupuesto presupuesto) {
        PresupuestoDTO dto = new PresupuestoDTO();
        dto.setId(presupuesto.getId());
        dto.setTitulo(presupuesto.getTitulo());
        dto.setPrecio(presupuesto.getPrecio());
        dto.setDescripcion(presupuesto.getDescripcion());
        dto.setFecha(presupuesto.getFecha());
        dto.setClienteId(
                presupuesto.getCliente() != null ? presupuesto.getCliente().getId() : null
        );
        return dto;
    }

    public Presupuesto toEntity(PresupuestoDTO dto) {
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setId(dto.getId());
        presupuesto.setTitulo(dto.getTitulo());
        presupuesto.setPrecio(dto.getPrecio());
        presupuesto.setDescripcion(dto.getDescripcion());
        presupuesto.setFecha(dto.getFecha());

        if (dto.getClienteId() == null) {
            throw new RuntimeException("clienteId requerido en PresupuestoDTO");
        }

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        presupuesto.setCliente(cliente);

        return presupuesto;
    }

    public List<PresupuestoDTO> findAllDTO() {
        return presupuestoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Page<Presupuesto> findAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return presupuestoRepository.findAll(pageable);
    }

}
