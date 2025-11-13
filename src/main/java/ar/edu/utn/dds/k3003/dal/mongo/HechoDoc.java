package ar.edu.utn.dds.k3003.dal.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HechoDoc {

    @Id
    private String _id;

    private String fuenteId;
    private String hechoId;
    private String nombreColeccion;

    @TextIndexed(weight = 10)
    private String titulo;

    private boolean eliminado;

    private Instant createdAt;
    private Instant updatedAt;

    @Builder.Default
    private List<PdiDoc> pdis = new ArrayList<>();
}