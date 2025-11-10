package ar.edu.utn.dds.k3003.bll.operacion;

import ar.edu.utn.dds.k3003.exceptions.NonTransientException;

public enum OperacionEnum {
    UPSERT,
    PDI,
    DELETE,
    DELETEALL;

    public static OperacionEnum GetFromString(String text) {
        return switch (text.trim().toLowerCase()) {
            case "upsert" -> UPSERT;
            case "delete" -> DELETE;
            case "deleteall" -> DELETEALL;
            case "pdi" -> PDI;
            default -> throw new NonTransientException("Operacion no reconocida: " + text);
        };
    }
}
