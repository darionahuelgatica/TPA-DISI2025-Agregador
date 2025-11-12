package ar.edu.utn.dds.k3003.dal.mongo;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class MongoDataAccess {

    private MongoTemplate mongoTemplate;
    private final String collection;

    public MongoDataAccess(MongoTemplate mongoTemplate,
                           @Value("${app.mongo.collection:hechos_search}") String collection) {
        this.mongoTemplate = mongoTemplate;
        this.collection = collection;
    }
    public void upsert(HechoDoc doc) {
        Query q = new Query(Criteria.where("_id").is(doc.getId()));
        Update u = new Update()
                .set("hechoId", doc.getHechoId())
                .set("fuenteId", doc.getFuenteId())
                .set("nombreColeccion", doc.getNombreColeccion())
                .set("titulo", doc.getTitulo())
                .set("etiquetas", doc.getEtiquetas())
                .set("eliminado", doc.isEliminado())
                .set("updatedAt", doc.getUpdatedAt())
                .setOnInsert("createdAt", doc.getCreatedAt());
        mongoTemplate.upsert(q, u, HechoDoc.class, this.collection);
    }
}
