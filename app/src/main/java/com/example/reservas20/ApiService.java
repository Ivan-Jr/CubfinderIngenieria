package com.example.reservas20;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("reservar.php")
    Call<ResponseBody> realizarReserva(
            @Field("fecha") String fecha,
            @Field("hora") String hora,
            @Field("num_personas") int numPersonas
    );

    @FormUrlEncoded
    @POST("cubiculo.php")
    Call<ResponseBody> realizarReservaCubiculo(
            @Field("fecha") String fecha,
            @Field("tiempo_uso") int tiempoUso,
            @Field("num_personas") int numPersonas
    );

    @FormUrlEncoded
    @POST("reservar_casillero.php")
    Call<ResponseBody> realizarReservaCasillero(
            @Field("fecha") String fecha,
            @Field("tiempo_uso") int tiempoUso
    );

    @FormUrlEncoded
    @POST("registrar.php") // Cambia esta ruta según tu archivo PHP para registro
    Call<ResponseBody> registrarUsuario(
            @Field("imagenPerfil") String imagenPerfil, // Ahora es el primero
            @Field("nombre") String nombre,
            @Field("carnet") String carnet,
            @Field("email") String email,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("iniciar_sesion.php")
    Call<ResponseBody> iniciarSesion(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("obtenerDatosUsuario.php")
    Call<Usuario> obtenerDatosUsuario(@Query("email") String email);

    @Multipart
    @POST("upload.php") // Ruta PHP que manejará la subida de imágenes
    Call<ResponseBody> subirImagen(
            @Part MultipartBody.Part imagenPerfil,
            @Part("nombre") RequestBody nombre,
            @Part("carnet") RequestBody carnet,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password
    );

}
