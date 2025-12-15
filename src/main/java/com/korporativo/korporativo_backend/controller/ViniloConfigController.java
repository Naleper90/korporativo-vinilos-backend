package com.korporativo.korporativo_backend.controller;

import com.korporativo.korporativo_backend.dto.ViniloConfigDTO;
import com.korporativo.korporativo_backend.model.ViniloConfig;
import com.korporativo.korporativo_backend.service.ViniloConfigService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vinilos")
@CrossOrigin(origins = "*")
public class ViniloConfigController {

    private final ViniloConfigService viniloConfigService;

    public ViniloConfigController(ViniloConfigService viniloConfigService) {
        this.viniloConfigService = viniloConfigService;
    }

    // LISTAR TODOS (DTO)
    @GetMapping
    public List<ViniloConfigDTO> listar() {
        return viniloConfigService.findAll()
                .stream()
                .map(viniloConfigService::toDTO)
                .collect(Collectors.toList());
    }

    // OBTENER UNO (DTO)
    @GetMapping("/{id}")
    public ResponseEntity<ViniloConfigDTO> obtener(@PathVariable Long id) {
        ViniloConfig vinilo = viniloConfigService.findById(id).get();
        ViniloConfigDTO dto = viniloConfigService.toDTO(vinilo);
        return ResponseEntity.ok(dto);
    }

    // CREAR (DTO + @Valid)
    @PostMapping
    public ResponseEntity<ViniloConfigDTO> crear(@Valid @RequestBody ViniloConfigDTO dto) {
        ViniloConfig entidad = viniloConfigService.toEntity(dto);
        ViniloConfig creado = viniloConfigService.create(entidad);
        ViniloConfigDTO creadoDTO = viniloConfigService.toDTO(creado);

        return ResponseEntity
                .created(URI.create("/api/vinilos/" + creadoDTO.getId()))
                .body(creadoDTO);
    }

    // ACTUALIZAR (DTO + @Valid)
    @PutMapping("/{id}")
    public ResponseEntity<ViniloConfigDTO> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody ViniloConfigDTO dto) {
        ViniloConfig entidad = viniloConfigService.toEntity(dto);
        ViniloConfig actualizado = viniloConfigService.update(id, entidad);
        ViniloConfigDTO actualizadoDTO = viniloConfigService.toDTO(actualizado);
        return ResponseEntity.ok(actualizadoDTO);
    }

    // ELIMINAR (solo ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        viniloConfigService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
