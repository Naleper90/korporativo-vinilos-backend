package com.korporativo.korporativo_backend.service;

import com.korporativo.korporativo_backend.dto.ViniloConfigDTO;
import com.korporativo.korporativo_backend.exception.RecursoNoEncontradoException;
import com.korporativo.korporativo_backend.model.Presupuesto;
import com.korporativo.korporativo_backend.model.ViniloConfig;
import com.korporativo.korporativo_backend.repository.PresupuestoRepository;
import com.korporativo.korporativo_backend.repository.ViniloConfigRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ViniloConfigService {

    private final ViniloConfigRepository viniloConfigRepository;
    private final PresupuestoRepository presupuestoRepository;

    public ViniloConfigService(ViniloConfigRepository viniloConfigRepository,
                               PresupuestoRepository presupuestoRepository) {
        this.viniloConfigRepository = viniloConfigRepository;
        this.presupuestoRepository = presupuestoRepository;
    }

    public List<ViniloConfig> findAll() {
        return viniloConfigRepository.findAll();
    }

    public Optional<ViniloConfig> findById(Long id) {
        return Optional.of(
                viniloConfigRepository.findById(id)
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException("ViniloConfig no encontrado: " + id)
                        )
        );
    }

    public ViniloConfig create(ViniloConfig vinilo) {
        return viniloConfigRepository.save(vinilo);
    }

    public ViniloConfig update(Long id, ViniloConfig datos) {
        ViniloConfig existente = viniloConfigRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("ViniloConfig no encontrado: " + id)
                );

        existente.setAnchoCm(datos.getAnchoCm());
        existente.setAltoCm(datos.getAltoCm());
        existente.setTipoVinilo(datos.getTipoVinilo());
        existente.setTipoCorte(datos.getTipoCorte());
        existente.setTipoAdhesivo(datos.getTipoAdhesivo());
        existente.setPais(datos.getPais());
        existente.setIncluirIva(datos.getIncluirIva());
        existente.setIncluirInstalacion(datos.getIncluirInstalacion());
        existente.setPrecioBase(datos.getPrecioBase());
        existente.setPrecioFinal(datos.getPrecioFinal());
        existente.setPresupuesto(datos.getPresupuesto());

        return viniloConfigRepository.save(existente);
    }

    public void delete(Long id) {
        ViniloConfig existente = viniloConfigRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("ViniloConfig no encontrado: " + id)
                );
        viniloConfigRepository.delete(existente);
    }

    public List<ViniloConfig> findByPresupuestoId(Long presupuestoId) {
        return viniloConfigRepository.findByPresupuestoId(presupuestoId);
    }

    // ---------- DTOs ----------

    public ViniloConfigDTO toDTO(ViniloConfig vinilo) {
        ViniloConfigDTO dto = new ViniloConfigDTO();
        dto.setId(vinilo.getId());
        dto.setAnchoCm(vinilo.getAnchoCm());
        dto.setAltoCm(vinilo.getAltoCm());
        dto.setTipoVinilo(vinilo.getTipoVinilo());
        dto.setTipoCorte(vinilo.getTipoCorte());
        dto.setTipoAdhesivo(vinilo.getTipoAdhesivo());
        dto.setPais(vinilo.getPais());
        dto.setIncluirIva(vinilo.getIncluirIva());
        dto.setIncluirInstalacion(vinilo.getIncluirInstalacion());
        dto.setPrecioBase(vinilo.getPrecioBase());
        dto.setPrecioFinal(vinilo.getPrecioFinal());
        dto.setPresupuestoId(
                vinilo.getPresupuesto() != null ? vinilo.getPresupuesto().getId() : null
        );
        return dto;
    }

    public ViniloConfig toEntity(ViniloConfigDTO dto) {
        ViniloConfig vinilo = new ViniloConfig();
        vinilo.setId(dto.getId());
        vinilo.setAnchoCm(dto.getAnchoCm());
        vinilo.setAltoCm(dto.getAltoCm());
        vinilo.setTipoVinilo(dto.getTipoVinilo());
        vinilo.setTipoCorte(dto.getTipoCorte());
        vinilo.setTipoAdhesivo(dto.getTipoAdhesivo());
        vinilo.setPais(dto.getPais());
        vinilo.setIncluirIva(dto.getIncluirIva());
        vinilo.setIncluirInstalacion(dto.getIncluirInstalacion());
        vinilo.setPrecioBase(dto.getPrecioBase());
        vinilo.setPrecioFinal(dto.getPrecioFinal());

        if (dto.getPresupuestoId() != null) {
            Presupuesto presupuesto = presupuestoRepository.findById(dto.getPresupuestoId())
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Presupuesto no encontrado: " + dto.getPresupuestoId())
                    );
            vinilo.setPresupuesto(presupuesto);
        }

        return vinilo;
    }

    public List<ViniloConfigDTO> findDTOsByPresupuestoId(Long presupuestoId) {
        return viniloConfigRepository.findByPresupuestoId(presupuestoId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
