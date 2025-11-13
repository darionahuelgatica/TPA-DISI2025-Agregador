package ar.edu.utn.dds.k3003.bll.operacion;

import ar.edu.utn.dds.k3003.dal.mongo.HechoDoc;
import ar.edu.utn.dds.k3003.dal.mongo.MongoDataAccess;
import ar.edu.utn.dds.k3003.dal.mongo.PdiDoc;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.utn.dds.k3003.bll.operacion.JsonHelpers.*;

@Component
public class PdiUpsertHandler implements IHandler {

    private final MongoDataAccess mongoDataAccess;
    private final ObjectMapper objectMapper;

    public PdiUpsertHandler(MongoDataAccess mongoDataAccess, ObjectMapper objectMapper) {
        this.mongoDataAccess = mongoDataAccess;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(String mensaje) {
        JsonNode root = toJsonNode(mensaje, objectMapper, OperacionEnum.PDIUPSERT);

        //PDI
        var pdi = new PdiDoc();
        setFromJsonNode(pdi, PdiDoc::setPdiId, root, "pdiId");
        setFromJsonNodeIfNotNull(pdi, PdiDoc::setDescripcion, root, "descripcion");
        setFromJsonNodeIfNotNull(pdi, PdiDoc::setUrl, root, "url");

        var tagsCsv = getFromJsonNodeIfNotNull(root, "etiquetas");
        if(tagsCsv != null)
            pdi.setEtiquetas(parseCsv(tagsCsv));

        //Hecho
        var hechoId = getFromJsonNode(root, "hechoId");
        var fuenteId = getFromJsonNode(root, "fuenteId");

        String hechoDocId = fuenteId + ":" + hechoId;

        HechoDoc hecho = mongoDataAccess.findById(hechoDocId);

        if(hecho == null) {
            hecho = new HechoDoc();
            hecho.set_id(hechoDocId);
            hecho.setFuenteId(fuenteId);
            hecho.setHechoId(hechoId);
            mongoDataAccess.upsert(hecho);
        }

        mongoDataAccess.upsertPdi(hechoDocId, pdi);
    }

    private static List<String> parseCsv(String csv) {
        if (csv == null || csv.isBlank()) return List.of();
        return Arrays.stream(csv.split(","))
            .map(String::trim)
            .filter(s -> !s.isBlank())
            .map(String::toLowerCase)
            .distinct()
            .collect(Collectors.toList());
    }
}