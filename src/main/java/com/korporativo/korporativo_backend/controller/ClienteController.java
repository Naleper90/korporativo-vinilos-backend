package com.korporativo.korporativo_backend.controller;

import com.korporativo.korporativo_backend.dto.ClienteDTO;
import com.korporativo.korporativo_backend.model.Cliente;
import com.korporativo.korporativo_backend.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // LISTAR (paginación + filtro opcional por nombre)
    @GetMapping
    public ResponseEntity<Page<ClienteDTO>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String nombre
    ) {
        Page<Cliente> pageClientes =
                clienteService.findAllPagedAndFiltered(page, size, nombre);

        List<ClienteDTO> contenido = pageClientes.getContent()
                .stream()
                .map(clienteService::toDTO)
                .collect(Collectors.toList());

        Page<ClienteDTO> pageDTO = new PageImpl<>(
                contenido,
                PageRequest.of(page, size),
                pageClientes.getTotalElements()
        );

        return ResponseEntity.ok(pageDTO);
    }

    // OBTENER UNO (DTO)
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtener(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id).get();
        ClienteDTO dto = clienteService.toDTO(cliente);
        return ResponseEntity.ok(dto);
    }

    // CREAR (DTO + @Valid)
    @PostMapping
    public ResponseEntity<ClienteDTO> crear(@Valid @RequestBody ClienteDTO clienteDTO) {
        Cliente entidad = clienteService.toEntity(clienteDTO);
        Cliente creado = clienteService.create(entidad);
        ClienteDTO creadoDTO = clienteService.toDTO(creado);

        return ResponseEntity
                .created(URI.create("/api/clientes/" + creadoDTO.getId()))
                .body(creadoDTO);
    }

    // ACTUALIZAR (DTO + @Valid)
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizar(@PathVariable Long id,
                                                 @Valid @RequestBody ClienteDTO datosDTO) {
        Cliente datos = clienteService.toEntity(datosDTO);
        Cliente actualizado = clienteService.update(id, datos);
        ClienteDTO actualizadoDTO = clienteService.toDTO(actualizado);
        return ResponseEntity.ok(actualizadoDTO);
    }

    // ELIMINAR (solo ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
