package com.korporativo.korporativo_backend.controller;

import com.korporativo.korporativo_backend.dto.ViniloConfigDTO;
import com.korporativo.korporativo_backend.exception.RecursoNoEncontradoException;
import com.korporativo.korporativo_backend.model.ViniloConfig;
import com.korporativo.korporativo_backend.service.ViniloConfigService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ViniloConfigController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ViniloConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ViniloConfigService viniloConfigService;

    @Test
    void obtenerViniloPorId_devuelve200yDTO() throws Exception {
        ViniloConfig vinilo = new ViniloConfig();
        vinilo.setId(1L);
        vinilo.setAnchoCm(100.0);
        vinilo.setAltoCm(50.0);
        vinilo.setTipoVinilo("Monomérico");
        vinilo.setTipoCorte("Recto");
        vinilo.setTipoAdhesivo("Fuerte");
        vinilo.setPais("España");
        vinilo.setIncluirIva(true);
        vinilo.setIncluirInstalacion(false);
        vinilo.setPrecioBase(80.0);
        vinilo.setPrecioFinal(100.0);

        ViniloConfigDTO dto = new ViniloConfigDTO();
        dto.setId(1L);
        dto.setAnchoCm(100.0);
        dto.setAltoCm(50.0);
        dto.setTipoVinilo("Monomérico");
        dto.setTipoCorte("Recto");
        dto.setTipoAdhesivo("Fuerte");
        dto.setPais("España");
        dto.setIncluirIva(true);
        dto.setIncluirInstalacion(false);
        dto.setPrecioBase(80.0);
        dto.setPrecioFinal(100.0);
        dto.setPresupuestoId(1L);

        Mockito.when(viniloConfigService.findById(anyLong()))
                .thenReturn(Optional.of(vinilo));

        Mockito.when(viniloConfigService.toDTO(vinilo))
                .thenReturn(dto);

        mockMvc.perform(get("/api/vinilos/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.precioBase").value(80.0))
                .andExpect(jsonPath("$.precioFinal").value(100.0));
    }

    @Test
    void obtenerViniloPorId_inexistente_devuelve404() throws Exception {
        Mockito.when(viniloConfigService.findById(anyLong()))
                .thenThrow(new RecursoNoEncontradoException("ViniloConfig no encontrado"));

        mockMvc.perform(get("/api/vinilos/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
