/*package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.model.SomeDomainObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AgregadorControllerTest {

    Fachada fachada;

    @BeforeEach
    void setUp() {
        fachada = new Fachada();            // Asigna a la variable de instancia, no una local
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void listarFuentes_DeberiaRetornarListaVacia() throws Exception {
        mockMvc.perform(get("/api/fuentes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void crearFuentes_DeberiaRetornarColeccionCreada() throws Exception {
        FuenteDTO nuevaFuente = new FuenteDTO("1", "Nombre de la Fuente", "Endpoint");

        mockMvc.perform(post("/api/fuentes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevaFuente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nombre").value("Nombre de la Fuente"));

        // Verificar que la colección fue creada
        mockMvc.perform(get("/api/fuentes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Nombre de la Fuente"));
    }
}*/