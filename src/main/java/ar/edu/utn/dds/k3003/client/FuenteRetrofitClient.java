package ar.edu.utn.dds.k3003.client;

import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface FuenteRetrofitClient {
    @GET("coleccion/{nombre_coleccion}/hechos")
    Call<List<HechoDTO>> get(@Query("nombre_coleccion") String nombre_coleccion);
}