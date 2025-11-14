package ar.edu.utn.dds.k3003.dto;

import ar.edu.utn.dds.k3003.dal.mongo.HechoDoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HechoDTO {

    public HechoDTO(String id,String nombreColeccion, String titulo) {
        this(id, nombreColeccion, titulo, null);
    }

    private String id;
    private String nombreColeccion;
    private String titulo;
    private LocalDateTime fecha;

    public static HechoDTO fromDoc(HechoDoc d) {
        var fecha = d.getCreatedAt() != null ? d.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() : null;

        return new HechoDTO(
            d.getHechoId(),
            d.getNombreColeccion(),
            d.getTitulo(),
            fecha
        );
    }
}