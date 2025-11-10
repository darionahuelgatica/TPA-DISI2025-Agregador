package ar.edu.utn.dds.k3003.dal.mongo;

import ar.edu.utn.dds.k3003.dal.model.Hecho;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class MongoDataAccess {

    private MongoTemplate mongoTemplate;
    private final String collection;

    public MongoDataAccess(MongoTemplate mongoTemplate,
                           @Value("${app.mongo.collection:hechos_search}") String collection) {
        this.mongoTemplate = mongoTemplate;
        this.collection = collection;
    }

    public void upsert(Hecho hecho) {
        String id = hecho.getFuenteId() + ":" + hecho.getHechoId();

        Query q = Query.query(Criteria.where("_id").is(id));
        Update u = new Update()
                .setOnInsert("_id", id)
                .set("hechoId", hecho.getHechoId())
                .set("fuenteId", hecho.getFuenteId())
                .set("titulo",hecho.getTitulo())
                .set("nombreColeccion",hecho.getNombreColeccion())
                .set("eliminado", false)
                .currentDate("updatedAt");
        if (hecho.getOccurredAt() != null) {
            u.setOnInsert("createdAt", hecho.getOccurredAt());
        } else {
            u.setOnInsert("createdAt", Instant.now());
        }

        mongoTemplate.getDb().getCollection(collection)
                .updateOne(q.getQueryObject(), u.getUpdateObject(), new com.mongodb.client.model.UpdateOptions().upsert(true));
    }
}
