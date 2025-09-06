package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.app.Consenso;
import ar.edu.utn.dds.k3003.app.ConsensoRequest;
import ar.edu.utn.dds.k3003.app.Fuente;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import io.javalin.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AgregadorController {

    @Autowired
    private final Fachada fachadaAgregador;

    public AgregadorController(Fachada fachadaAgregador) {
        this.fachadaAgregador = fachadaAgregador;
    }

    // GET /fuentes
    @GetMapping("/fuentes")
    public List<FuenteDTO> listarFuentes() {
        return fachadaAgregador.fuentes();
    }

    // POST /fuentes
    @PostMapping("/fuentes")
    public ResponseEntity<FuenteDTO> agregarFuente(@RequestBody FuenteDTO fuenteDTO) {
        FuenteDTO fuenteCreada = fachadaAgregador.agregar(fuenteDTO);
        return ResponseEntity.ok(fuenteCreada);
    }

    // GET /coleccion/{nombre}/hechos
    @GetMapping("/coleccion/{nombre}/hechos")
    public List<HechoDTO> hechosDeColeccion(@PathVariable String nombre) {
        // Esto va a devolver todos los hechos de la colección con el nombre dado
        return fachadaAgregador.hechos(nombre);
    }

    // Endpoint raíz de prueba para validar que el controller funciona
    @GetMapping("/coleccion/test")
    public String test() {
        return "ColeccionController activo ✅";
    }


    // PATCH /consenso
    @PatchMapping("/consenso")
    public ResponseEntity<Void> cambiarConsenso(@RequestBody ConsensoRequest request) {
        ConsensosEnum tipo = switch (request.getTipo().toLowerCase()) {
            case "todos" -> ConsensosEnum.TODOS;
            case "almenos2" -> ConsensosEnum.AL_MENOS_2;
            default -> throw new IllegalArgumentException("Tipo de consenso no válido");
        };

        fachadaAgregador.setConsensoStrategy(tipo, request.getColeccion());
        return ResponseEntity.ok().build();
    }

}

