package com.korporativo.korporativo_backend.service;

import com.korporativo.korporativo_backend.dto.ClienteDTO;
import com.korporativo.korporativo_backend.exception.RecursoNoEncontradoException;
import com.korporativo.korporativo_backend.model.Cliente;
import com.korporativo.korporativo_backend.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(Long id) {
        return Optional.of(
                clienteRepository.findById(id)
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException("Cliente no encontrado: " + id)
                        )
        );
    }

    public Cliente create(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente update(Long id, Cliente datos) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cliente no encontrado: " + id)
                );

        existente.setNombre(datos.getNombre());
        existente.setEmail(datos.getEmail());
        existente.setTelefono(datos.getTelefono());
        existente.setEmpresa(datos.getEmpresa());

        return clienteRepository.save(existente);
    }

    public void delete(Long id) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cliente no encontrado: " + id)
                );
        clienteRepository.delete(existente);
    }

    public ClienteDTO toDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setEmail(cliente.getEmail());
        dto.setTelefono(cliente.getTelefono());
        dto.setEmpresa(cliente.getEmpresa());
        return dto;
    }

    public Cliente toEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setId(dto.getId());
        cliente.setNombre(dto.getNombre());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmpresa(dto.getEmpresa());
        return cliente;
    }

    // Paginación básica con orden por nombre ascendente
    public Page<Cliente> findAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
        return clienteRepository.findAll(pageable);
    }

    // Paginación + filtro opcional por nombre (contiene, ignore case)
    public Page<Cliente> findAllPagedAndFiltered(int page, int size, String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return findAllPaged(page, size);
        }

        List<Cliente> filtrados = clienteRepository.findByNombreContainingIgnoreCase(nombre);
        int start = Math.min(page * size, filtrados.size());
        int end = Math.min(start + size, filtrados.size());

        List<Cliente> contenidoPagina = filtrados.subList(start, end);

        return new PageImpl<>(
                contenidoPagina,
                PageRequest.of(page, size, Sort.by("nombre").ascending()),
                filtrados.size()
        );
    }
}
