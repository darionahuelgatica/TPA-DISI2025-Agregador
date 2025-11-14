package ar.edu.utn.dds.k3003.dal.mongo;

import com.mongodb.client.result.UpdateResult;
import org.springframework.lang.Nullable;
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
                           @Value("${app.mongo.collection}") String collection) {
        this.mongoTemplate = mongoTemplate;
        this.collection = collection;
    }

    public void upsert(HechoDoc doc) {
        Query q = new Query(Criteria.where("_id").is(doc.get_id()));
        Update u = new Update()
            .set("hechoId", doc.getHechoId())
            .set("fuenteId", doc.getFuenteId())
            .set("nombreColeccion", doc.getNombreColeccion())
            .set("titulo", doc.getTitulo())
            .set("eliminado", doc.isEliminado())
            .set("updatedAt", doc.getUpdatedAt())
            .setOnInsert("createdAt", doc.getCreatedAt());
        var result = mongoTemplate.updateFirst(q, u, HechoDoc.class, this.collection);
        if (result.getMatchedCount() == 0) {
            mongoTemplate.insert(doc, this.collection);
        }
    }

    public void delete(String hechoDocId) {
        Query q = new Query(Criteria.where("_id").is(hechoDocId));
        Update u = new Update().set("eliminado", true).set("updatedAt", Instant.now());
        mongoTemplate.upsert(q, u, HechoDoc.class, this.collection);
    }

    public void deleteAll() {
        mongoTemplate.remove(new Query(), HechoDoc.class, this.collection);
    }

    @Nullable
    public HechoDoc findById(String id){
        Query q = new Query(Criteria.where("_id").is(id));
        var hecho = mongoTemplate.findOne(q, HechoDoc.class, this.collection);
        return hecho;
    }

    public void upsertPdi(String hechoDocId, PdiDoc pdi) {
        Query query = Query.query(
                Criteria.where("_id").is(hechoDocId)
                        .and("pdis.pdiId").is(pdi.getPdiId())
        );

        Update update = new Update()
                .set("pdis.$.descripcion", pdi.getDescripcion())
                .set("pdis.$.url", pdi.getUrl())
                .set("pdis.$.etiquetas", pdi.getEtiquetas());

        UpdateResult result =
                mongoTemplate.updateFirst(query, update, this.collection);

        if (result.getMatchedCount() == 0) {
            mongoTemplate.updateFirst(
                    Query.query(Criteria.where("_id").is(hechoDocId)),
                    new Update().push("pdis", pdi),
                    this.collection
            );
        }
    }

    public void deletePdi(String hechoDocId, String pdiId) {
        Query query = Query.query(
                Criteria.where("_id").is(hechoDocId)
        );

        Update update = new Update()
            .pull("pdis", Query.query(
                Criteria.where("pdiId").is(pdiId)
            ));

        UpdateResult result = mongoTemplate.updateFirst(query, update, this.collection);

        if (result.getModifiedCount() == 0) {
            System.out.println("No se eliminó ningún PDI. Puede que no exista: " + pdiId);
        }
    }

    public void deleteAllPdi(){
        Update update = new Update().unset("pdis");
        mongoTemplate.updateMulti(new Query(), update, this.collection);
    }
}