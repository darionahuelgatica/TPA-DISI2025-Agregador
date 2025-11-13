package ar.edu.utn.dds.k3003.bll.operacion;

import ar.edu.utn.dds.k3003.exceptions.NonTransientException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.BiConsumer;

public class JsonHelpers {

    public static JsonNode toJsonNode(String mensaje, ObjectMapper objectMapper, OperacionEnum op){
        try
        {
            return objectMapper.readTree(mensaje);
        }
        catch (Exception e) {
            throw new NonTransientException("No se pudo parsear el mensaje de " + op.toString() + ": " + mensaje);
        }
    }

    public static String getFromJsonNode(JsonNode root, String key) {
        JsonNode item = root.get(key);
        if (item == null || item.isMissingNode()) {
            throw new NonTransientException("Falta " + key + " en el mensaje " + root.toPrettyString());
        }
        return item.asText();
    }

    public static String getFromJsonNodeIfNotNull(JsonNode root, String key) {
        JsonNode item = root.get(key);
        if (item == null || item.isMissingNode()) {
            return null;
        }
        return item.asText();
    }

    public static <T> void setFromJsonNode(T doc, BiConsumer<T, String> setter, JsonNode root, String key) {
        JsonNode item = root.get(key);
        if (item == null || item.isMissingNode()) {
            throw new NonTransientException("Falta " + key + " en el mensaje " + root.toPrettyString());
        }
        setter.accept(doc, item.asText());
    }

    public static <T> void setFromJsonNodeIfNotNull(T doc, BiConsumer<T, String> setter, JsonNode root, String key) {
        JsonNode item = root.get(key);
        if (item != null && !item.isMissingNode()) {
            setter.accept(doc, item.asText());
        }
    }
}
