package ar.edu.utn.dds.k3003.web.controllers;

import ar.edu.utn.dds.k3003.web.bll.services.IFuenteService;
import ar.edu.utn.dds.k3003.web.dto.FuenteDTO;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/fuentes")
public class FuenteController {

    private final IFuenteService fuenteService;
    private final MeterRegistry meterRegistry;

    @Autowired
    public FuenteController(IFuenteService fuenteService, MeterRegistry meterRegistry) {
        this.fuenteService = fuenteService;
        this.meterRegistry = meterRegistry;
    }

    @GetMapping
    public ResponseEntity<List<FuenteDTO>> listarFuentes() {
        meterRegistry.counter("agregador.fuentes.listadas").increment();
        return ResponseEntity.ok(fuenteService.listFuentes());
    }

    @GetMapping("{fuenteId}")
    public ResponseEntity<FuenteDTO> getFuenteById(@PathVariable String fuenteId) {
        meterRegistry.counter("agregador.fuentes.obtenidas").increment();
        return  ResponseEntity.ok(fuenteService.getFuenteById(fuenteId));
    }

    @PostMapping
    public ResponseEntity<FuenteDTO> agregarFuente(@RequestBody FuenteDTO fuenteDTO) {
        try {
            meterRegistry.counter("agregador.fuentes.creadas").increment();
            FuenteDTO fuenteCreada = fuenteService.addFuente(fuenteDTO);
            return ResponseEntity.ok(fuenteCreada);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> borrarFuentes(@RequestParam(value = "fuenteId", required = false) String fuenteId) {
        if(fuenteId != null)
            try{ fuenteService.borrarFuente(fuenteId); } catch (NoSuchElementException e){ return ResponseEntity.notFound().build(); }
        else
            fuenteService.borrarTodasLasFuentes();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}