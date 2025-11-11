package ar.edu.utn.dds.k3003.dto;

import ar.edu.utn.dds.k3003.dal.mongo.HechoDoc;
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

    public HechoDTO(String id, String nombreColeccion, String titulo, List<String> etiquetas, LocalDateTime fecha) {
        this(id, nombreColeccion, titulo, etiquetas, null, fecha, null);
    }

    private String id;
    private String nombreColeccion;
    private String titulo;
    private List<String> etiquetas;
    private String ubicacion;
    private LocalDateTime fecha;
    private String origen;

    public static HechoDTO from(HechoDoc d) {
        return new HechoDTO(
            d.getHechoId(),
            d.getNombreColeccion(),
            d.getTitulo(),
            d.getEtiquetas(),
            d.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
        );
    }
}