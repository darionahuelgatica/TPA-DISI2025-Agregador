package ar.edu.utn.dds.k3003.bll.operacion;

import ar.edu.utn.dds.k3003.dal.mongo.MongoDataAccess;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import static ar.edu.utn.dds.k3003.bll.operacion.JsonHelpers.*;

@Component
public class PdiDeleteHandler implements IHandler {

    private final MongoDataAccess mongoDataAccess;
    private final ObjectMapper objectMapper;

    public PdiDeleteHandler(MongoDataAccess mongoDataAccess, ObjectMapper objectMapper) {
        this.mongoDataAccess = mongoDataAccess;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(String mensaje) {
        System.out.println("PDI DELETE " + mensaje);

        var json = toJsonNode(mensaje, objectMapper, OperacionEnum.PDIDELETE);

        var fuenteId = getFromJsonNode(json, "fuenteId");
        var hechoId = getFromJsonNode(json, "hechoId");
        var pdiId = getFromJsonNode(json, "pdiId");

        String hechoDocId = fuenteId + ":" + hechoId;
        mongoDataAccess.deletePdi(hechoDocId, pdiId);
    }
}
