package ar.edu.utn.dds.k3003.web.dal.client;

import ar.edu.utn.dds.k3003.web.dto.SolicitudDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import java.util.List;

public interface SolicitudesRetrofitClient {
    @GET("api/solicitudes")
    Call<List<SolicitudDTO>> listByHechoId(@Query("hecho") String hechoId);
}