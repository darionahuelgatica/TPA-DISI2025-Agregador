package ar.edu.utn.dds.k3003.web.dal.client;

import ar.edu.utn.dds.k3003.web.dto.HechoDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface FuenteRetrofitClient {
    @GET("colecciones/{nombre_coleccion}/hechos")
    Call<List<HechoDTO>> get(@Path("nombre_coleccion") String nombre_coleccion);
}