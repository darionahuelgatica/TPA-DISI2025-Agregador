package ar.edu.utn.dds.k3003.bll.operacion;

import ar.edu.utn.dds.k3003.exceptions.NonTransientException;

public enum OperacionEnum {
    UPSERT,
    DELETE,
    DELETEALL,
    PDIUPSERT,
    PDIDELETE,
    PDIDELETEALL;

    public static OperacionEnum GetFromString(String text) {
        return switch (text.trim().toLowerCase()) {
            case "upsert" -> UPSERT;
            case "delete" -> DELETE;
            case "deleteall" -> DELETEALL;
            case "pdiupsert" -> PDIUPSERT;
            case "pdidelete" -> PDIDELETE;
            case "pdideleteall" -> PDIDELETEALL;
            default -> throw new NonTransientException("Operacion no reconocida: " + text);
        };
    }
}
