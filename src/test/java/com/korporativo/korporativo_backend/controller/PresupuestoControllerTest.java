package com.korporativo.korporativo_backend.controller;

import com.korporativo.korporativo_backend.dto.PresupuestoDTO;
import com.korporativo.korporativo_backend.exception.RecursoNoEncontradoException;
import com.korporativo.korporativo_backend.model.Presupuesto;
import com.korporativo.korporativo_backend.service.PresupuestoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PresupuestoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class PresupuestoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PresupuestoService presupuestoService;

    @Test
    void obtenerPresupuestoPorId_devuelve200yDTO() throws Exception {
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setId(1L);
        presupuesto.setTitulo("Título test");
        presupuesto.setPrecio(100.0);
        presupuesto.setDescripcion("Descripción test");
        presupuesto.setFecha(LocalDate.of(2025, 1, 1));

        PresupuestoDTO dto = new PresupuestoDTO();
        dto.setId(1L);
        dto.setTitulo("Título test");
        dto.setPrecio(100.0);
        dto.setDescripcion("Descripción test");
        dto.setFecha(LocalDate.of(2025, 1, 1));
        dto.setClienteId(1L);

        Mockito.when(presupuestoService.getPresupuestoById(anyLong()))
                .thenReturn(presupuesto);

        Mockito.when(presupuestoService.toDTO(presupuesto))
                .thenReturn(dto);

        mockMvc.perform(get("/api/presupuestos/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Título test"))
                .andExpect(jsonPath("$.precio").value(100.0));
    }

    @Test
    void obtenerPresupuestoPorId_inexistente_devuelve404() throws Exception {
        Mockito.when(presupuestoService.getPresupuestoById(anyLong()))
                .thenThrow(new RecursoNoEncontradoException("Presupuesto no encontrado"));

        mockMvc.perform(get("/api/presupuestos/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
