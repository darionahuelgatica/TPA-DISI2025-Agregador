package ar.edu.utn.dds.k3003.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HechoDTO {
    public HechoDTO(String id,String nombreColeccion, String titulo) {
        this(id, nombreColeccion, titulo, null, null, null, null);
    }

    private String id;
    private String nombreColeccion;
    private String titulo;
    private List<String> etiquetas;
    private String ubicacion;
    private LocalDateTime fecha;
    private String origen;
}