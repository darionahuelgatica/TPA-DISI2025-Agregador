package ar.edi.itn.dds.k3003.model;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.model.SomeDomainObject;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class SomeDomainModelTest {

    Fachada fachada;
    private FachadaFuente fuente1;
    private FachadaFuente fuente2;
    private FachadaFuente fachadaFuente;
    SomeDomainObject someDomainObject1;

    SomeDomainObject someDomainObject2;

    @BeforeEach
    void setUp() {
        fachada = new Fachada(/*fachadaFuente*/);            // Asigna a la variable de instancia, no una local
        fuente1 = Mockito.mock(FachadaFuente.class);
        fuente2 = Mockito.mock(FachadaFuente.class);

        someDomainObject1 = new SomeDomainObject("a", 1L);
        someDomainObject2 = new SomeDomainObject("b", 2L);
    }

    @Test
    void testSum() {
        var result = someDomainObject1.sum(someDomainObject2);
        assertEquals("ab", result.getAnAttribute());
        assertEquals(3L, result.getOtherAttribute());

    }

    @Test
    void testBuscarFuenteXIdNoExistente() {

        String idInexistente = UUID.randomUUID().toString();
        NoSuchElementException ex = assertThrows(
                NoSuchElementException.class,
                () -> fachada.buscarFuenteXId(idInexistente),
                "Debe lanzar NoSuchElementException si la fuente no existe"
        );
        assertTrue(ex.getMessage().contains("no existe"),
                "El mensaje de excepción debe indicar que no se encontró la fuente");
    }

    @Test
    void testListarVariasFuentes() {
        // Agregar 3 fuentes con IDs explícitos
        FuenteDTO f1 = new FuenteDTO("id1", "F1", "http://1");
        FuenteDTO f2 = new FuenteDTO("id2", "F2", "http://2");
        FuenteDTO f3 = new FuenteDTO("id3", "F3", "http://3");
        fachada.agregar(f1);
        fachada.agregar(f2);
        fachada.agregar(f3);

        List<FuenteDTO> todas = fachada.fuentes();
    }

}