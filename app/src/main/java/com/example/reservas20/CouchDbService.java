package com.example.reservas20;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CouchDbService {

    @POST("reservas") // Para crear un nuevo documento
    Call<CouchDbResponse> reservarMesa(@Body Reserva reserva);

    @GET("reservas/{id}") // Para obtener un documento existente
    Call<Reserva> obtenerReserva(@Path("id") String id);

    @PUT("reservas/{id}") // Para actualizar un documento existente
    Call<CouchDbResponse> actualizarMesa(@Path("id") String id, @Body Reserva reserva);
}
