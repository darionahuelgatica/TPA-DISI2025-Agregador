package ar.edu.utn.dds.k3003.bll.handlers;

import ar.edu.utn.dds.k3003.dal.model.Hecho;
import ar.edu.utn.dds.k3003.dal.mongo.MongoDataAccess;
import ar.edu.utn.dds.k3003.exceptions.NonTransientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class UpsertHandler implements IHandler {

    private final MongoDataAccess mongoDataAccess;
    private final ObjectMapper objectMapper;

    public UpsertHandler(MongoDataAccess mongoDataAccess, ObjectMapper objectMapper) {
        this.mongoDataAccess = mongoDataAccess;
        this.objectMapper = objectMapper;
    }

    public void handle(String mensaje) {

        System.out.println("UPSERT " + mensaje);

        var hecho = new Hecho();
        try
        {
            JsonNode rootNode = objectMapper.readTree(mensaje);
            var hechoId = rootNode.get("hechoId");
            if(hechoId == null || hechoId.isMissingNode())
                throw new NonTransientException("Falta hechoId: " + mensaje);

            var fuenteId = rootNode.get("fuenteId");
            if(fuenteId == null || fuenteId.isMissingNode())
                throw new NonTransientException("Falta fuenteId: " + mensaje);

            hecho.set_id(fuenteId.asText() + ":" + hechoId.asText());
            hecho.setHechoId(hechoId.asText());
            hecho.setFuenteId(fuenteId.asText());

            var titulo = rootNode.get("titulo");
            if(titulo != null && !titulo.isMissingNode())
                hecho.setTitulo(titulo.asText());

            var nombreColeccion = rootNode.get("nombreColeccion");
            if(nombreColeccion != null && !nombreColeccion.isMissingNode())
                hecho.setNombreColeccion(nombreColeccion.asText());
        }
        catch (java.io.IOException ex) {
            throw new NonTransientException("No se pudo parsear el mensaje de upsert: " + mensaje);
        }

        this.mongoDataAccess.upsert(hecho);
    }
}
