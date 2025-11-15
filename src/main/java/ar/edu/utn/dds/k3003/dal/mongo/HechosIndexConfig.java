package ar.edu.utn.dds.k3003.dal.mongo;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.*;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class HechosIndexConfig {

    private final MongoTemplate mongo;

    @EventListener(ApplicationReadyEvent.class)
    public void ensureIndexes() {
        var ops = mongo.indexOps(HechoDoc.class);

        var text = new TextIndexDefinition.TextIndexDefinitionBuilder()
                .onField("titulo", 10F)
                .onField("nombreColeccion")
                .onField("hechoId")
                //.onField("pdis.descripcion")
                .build();
        ops.ensureIndex(text);

        //ops.ensureIndex(new Index()
                //.on("pdis.etiquetas", Sort.Direction.ASC)); // multikey
    }
}