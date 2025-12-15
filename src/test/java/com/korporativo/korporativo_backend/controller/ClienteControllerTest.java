package com.korporativo.korporativo_backend.controller;

import com.korporativo.korporativo_backend.dto.ClienteDTO;
import com.korporativo.korporativo_backend.exception.RecursoNoEncontradoException;
import com.korporativo.korporativo_backend.model.Cliente;
import com.korporativo.korporativo_backend.service.ClienteService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClienteController.class) // test de capa web
@AutoConfigureMockMvc(addFilters = false)         // seguridad desactivada aquí
@ActiveProfiles("test")                           // usa perfil test (no corre initUsers)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService; // se mockea la capa de servicio

    @Test
    void obtenerClientePorId_devuelve200yClienteDTO() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");

        // lo que devuelve el service al buscar
        Mockito.when(clienteService.findById(anyLong()))
                .thenReturn(Optional.of(cliente));

        // cómo se transforma a DTO
        ClienteDTO dto = new ClienteDTO();
        dto.setId(1L);
        dto.setNombre("Juan");

        Mockito.when(clienteService.toDTO(cliente))
                .thenReturn(dto);

        mockMvc.perform(get("/api/clientes/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void obtenerClientePorId_inexistente_devuelve404() throws Exception {
        Mockito.when(clienteService.findById(anyLong()))
                .thenThrow(new RecursoNoEncontradoException("Cliente no encontrado: 99"));

        mockMvc.perform(get("/api/clientes/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearCliente_valido_devuelve201yDTO() throws Exception {
        ClienteDTO dtoEntrada = new ClienteDTO();
        dtoEntrada.setNombre("Juan");
        dtoEntrada.setEmail("juan@example.com");
        dtoEntrada.setTelefono("600000000");
        dtoEntrada.setEmpresa("Empresa X");

        Cliente entidadGuardada = new Cliente();
        entidadGuardada.setId(1L);
        entidadGuardada.setNombre("Juan");
        entidadGuardada.setEmail("juan@example.com");
        entidadGuardada.setTelefono("600000000");
        entidadGuardada.setEmpresa("Empresa X");

        ClienteDTO dtoSalida = new ClienteDTO();
        dtoSalida.setId(1L);
        dtoSalida.setNombre("Juan");
        dtoSalida.setEmail("juan@example.com");
        dtoSalida.setTelefono("600000000");
        dtoSalida.setEmpresa("Empresa X");

        Mockito.when(clienteService.toEntity(Mockito.any(ClienteDTO.class)))
                .thenReturn(entidadGuardada);

        Mockito.when(clienteService.create(Mockito.any(Cliente.class)))
                .thenReturn(entidadGuardada);

        Mockito.when(clienteService.toDTO(entidadGuardada))
                .thenReturn(dtoSalida);

        String jsonEntrada = """
            {
            "nombre": "Juan",
            "email": "juan@example.com",
            "telefono": "600000000",
            "empresa": "Empresa X"
            }
            """;

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntrada))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void crearCliente_invalido_devuelve400yErrorValidacion() throws Exception {
        String jsonInvalido = """
            {
            "nombre": "",
            "email": "no-es-email",
            "telefono": "",
            "empresa": ""
            }
            """;

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

}
