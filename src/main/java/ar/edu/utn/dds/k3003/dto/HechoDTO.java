package ar.edu.utn.dds.k3003.dto;

import ar.edu.utn.dds.k3003.dal.mongo.HechoDoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HechoDTO {

    private String id;
    private String nombreColeccion;
    private String titulo;

    public static HechoDTO fromDoc(HechoDoc d) {
        return new HechoDTO(
            d.getHechoId(),
            d.getNombreColeccion(),
            d.getTitulo()
        );
    }
}