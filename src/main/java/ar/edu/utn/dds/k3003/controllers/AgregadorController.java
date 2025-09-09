package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.consenso.ConsensoRequest;
import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AgregadorController {

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
        try {
            FuenteDTO fuenteCreada = fachadaAgregador.agregar(fuenteDTO);
            return ResponseEntity.ok(fuenteCreada);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // GET /coleccion/{coleccion}/hechos
    @GetMapping("/coleccion/{coleccion}/hechos")
    public List<HechoDTO> hechosDeColeccion(@PathVariable String coleccion) {
        // Esto va a devolver todos los hechos de la colección con el nombre dado
        return fachadaAgregador.hechos(coleccion);
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

