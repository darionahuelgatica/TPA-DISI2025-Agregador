package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.app.FachadaAgregadorExtended;
import ar.edu.utn.dds.k3003.consenso.ConsensoRequest;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
public class AgregadorController {

    private final FachadaAgregadorExtended fachadaAgregador;
    private final MeterRegistry meterRegistry;

    public AgregadorController(FachadaAgregadorExtended fachadaAgregador, MeterRegistry meterRegistry) {
        this.fachadaAgregador = fachadaAgregador;
        this.meterRegistry = meterRegistry;
    }

    // GET /fuentes
    @GetMapping("/fuentes")
    public List<FuenteDTO> listarFuentes() {
        meterRegistry.counter("agregador.fuentes.listadas").increment();
        return fachadaAgregador.fuentes();
    }

    // POST /fuentes
    @PostMapping("/fuentes")
    public ResponseEntity<FuenteDTO> agregarFuente(@RequestBody FuenteDTO fuenteDTO) {
        try {
            meterRegistry.counter("agregador.fuentes.creadas").increment();
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

    @DeleteMapping("/fuentes")
    public ResponseEntity<Void> borrarFuentes(@RequestParam(value = "fuenteId", required = false) String fuenteId) {
        if(fuenteId != null)
            try{ fachadaAgregador.borrarFuente(fuenteId); } catch (NoSuchElementException e){ return ResponseEntity.notFound().build(); }
        else
            fachadaAgregador.borrarTodasLasFuentes();

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // GET /coleccion/{coleccion}/hechos
    @GetMapping("/coleccion/{coleccion}/hechos")
    public List<HechoDTO> hechosDeColeccion(@PathVariable String coleccion) {
        meterRegistry.counter("agregador.hechos.listados").increment();
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
        meterRegistry.counter("agregador.consenso.actualizado").increment();
        return ResponseEntity.ok().build();
    }
}

