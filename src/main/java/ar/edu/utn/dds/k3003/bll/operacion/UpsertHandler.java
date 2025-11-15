package ar.edu.utn.dds.k3003.bll.operacion;

import ar.edu.utn.dds.k3003.dal.mongo.HechoDoc;
import ar.edu.utn.dds.k3003.dal.mongo.MongoDataAccess;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import java.time.Instant;
import static ar.edu.utn.dds.k3003.bll.operacion.JsonHelpers.*;

@Component
public class UpsertHandler implements IHandler {

    private final MongoDataAccess mongoDataAccess;
    private final ObjectMapper objectMapper;

    public UpsertHandler(MongoDataAccess mongoDataAccess, ObjectMapper objectMapper) {
        this.mongoDataAccess = mongoDataAccess;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(String mensaje) {
        System.out.println("UPSERT " + mensaje);

        final HechoDoc doc = new HechoDoc();

        JsonNode root = toJsonNode(mensaje, objectMapper, OperacionEnum.UPSERT);

        String hechoId = getFromJsonNode(root, "hechoId");
        String fuenteId = getFromJsonNode(root, "fuenteId");

        String id = fuenteId + ":" + hechoId;
        doc.set_id(id);
        doc.setHechoId(hechoId);
        doc.setFuenteId(fuenteId);

        setFromJsonNodeIfNotNull(doc, HechoDoc::setTitulo, root, "titulo");
        setFromJsonNodeIfNotNull(doc, HechoDoc::setNombreColeccion, root, "nombreColeccion");

        this.mongoDataAccess.upsert(doc);
    }
}