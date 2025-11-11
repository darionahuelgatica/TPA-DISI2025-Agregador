package ar.edu.utn.dds.k3003.dal.mongo;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.index.TextIndexDefinition.TextIndexDefinitionBuilder;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.domain.Sort;

@Configuration
public class HechosTextIndexConfig {

    @Bean
    CommandLineRunner ensureHechosIndexes(MongoTemplate mongo) {
        return args -> {
            var textIdx = new TextIndexDefinitionBuilder()
                    .onField("_id")
                    .onField("fuenteId")
                    .onField("hechoId")
                    .onField("nombreColeccion")
                    .onField("titulo")
                    .withDefaultLanguage("spanish")
                    .build();

            mongo.indexOps(HechoDoc.class).ensureIndex((TextIndexDefinition) textIdx);

            mongo.indexOps(HechoDoc.class).ensureIndex(new Index().on("etiquetas", Sort.Direction.ASC));
            mongo.indexOps(HechoDoc.class).ensureIndex(new Index().on("eliminado", Sort.Direction.ASC));
            mongo.indexOps(HechoDoc.class).ensureIndex(new Index().on("updatedAt", Sort.Direction.DESC));
        };
    }
}