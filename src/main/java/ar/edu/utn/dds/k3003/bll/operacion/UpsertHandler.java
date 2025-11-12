package ar.edu.utn.dds.k3003.bll.operacion;

import ar.edu.utn.dds.k3003.dal.mongo.HechoDoc;
import ar.edu.utn.dds.k3003.dal.mongo.MongoDataAccess;
import ar.edu.utn.dds.k3003.exceptions.NonTransientException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

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

        try {
            JsonNode root = objectMapper.readTree(mensaje);

            JsonNode hechoId = root.get("hechoId");
            if (hechoId == null || hechoId.isMissingNode() || hechoId.asText().isBlank()) {
                throw new NonTransientException("Falta hechoId: " + mensaje);
            }

            JsonNode fuenteId = root.get("fuenteId");
            if (fuenteId == null || fuenteId.isMissingNode() || fuenteId.asText().isBlank()) {
                throw new NonTransientException("Falta fuenteId: " + mensaje);
            }

            String id = fuenteId.asText() + ":" + hechoId.asText();
            doc.setId(id);
            doc.setHechoId(hechoId.asText());
            doc.setFuenteId(fuenteId.asText());

            JsonNode titulo = root.get("titulo");
            if (titulo != null && !titulo.isMissingNode()) {
                doc.setTitulo(titulo.asText());
            }

            JsonNode nombreColeccion = root.get("nombreColeccion");
            if (nombreColeccion != null && !nombreColeccion.isMissingNode()) {
                doc.setNombreColeccion(nombreColeccion.asText());
            }

            List<String> etiquetas = new ArrayList<>();
            JsonNode etiquetasNode = root.get("etiquetas");
            if (etiquetasNode != null && etiquetasNode.isArray()) {
                StreamSupport.stream(etiquetasNode.spliterator(), false)
                        .map(JsonNode::asText)
                        .filter(s -> s != null && !s.isBlank())
                        .distinct()
                        .forEach(etiquetas::add);
            } else {
                JsonNode etiquetasCsv = root.get("etiquetasCsv");
                if (etiquetasCsv != null && !etiquetasCsv.isMissingNode()) {
                    for (String s : etiquetasCsv.asText("").split(",")) {
                        String t = s.trim();
                        if (!t.isBlank() && !etiquetas.contains(t)) etiquetas.add(t);
                    }
                }
            }
            if (!etiquetas.isEmpty()) {
                doc.setEtiquetas(etiquetas);
            }

            JsonNode eliminado = root.get("eliminado");
            doc.setEliminado(eliminado != null && eliminado.asBoolean(false));

            Instant now = Instant.now();
            doc.setUpdatedAt(now);
            if (doc.getCreatedAt() == null) {
                doc.setCreatedAt(now);
            }

        } catch (Exception e) {
            if (e instanceof NonTransientException) throw (NonTransientException) e;
            throw new NonTransientException("No se pudo parsear el mensaje de upsert: " + mensaje);
        }

        this.mongoDataAccess.upsert(doc);
    }
}
