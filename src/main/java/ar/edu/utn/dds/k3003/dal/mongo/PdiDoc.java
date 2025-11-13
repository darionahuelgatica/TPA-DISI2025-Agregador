package ar.edu.utn.dds.k3003.dal.mongo;

import lombok.*;
import org.springframework.data.mongodb.core.index.TextIndexed;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PdiDoc {
    private String pdiId;
    @TextIndexed
    private String descripcion;
    private String url;

    private List<String> etiquetas;
}