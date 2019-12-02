package com.example.congnitusfirma.dao

import com.example.congnitusfirma.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    //http://35.155.161.8:8080/WSExample/DataWS?ApiCall=getLoginUsr&email=juan@hola.com.mx&nip=123
   // http://35.155.161.8:8080/WSExample/DataWS?ApiCall=getLoginUsr&
    //http://35.155.161.8:8080/WSExample/ DataWS?ApiCall=getLoginUsr &email= juan@hola.com.mx &nip= 123
    @POST("DataWS?ApiCall=getLoginUsr")
    fun loginPost(@Query("email") email: String,
                  @Query("nip") password: String): Call<UsuariosResponse>


    @GET("DataWS?ApiCall=setRegisterUsr")
    fun registroUser(
        @Query ("email") correoR:String,
        @Query ("nombre") nombreR:String,
        @Query ("app") apellidoPR:String,
        @Query ("apm") apellidoMR:String,
        @Query ("nip") contraR:String
    ): Call<ResponseRegistro>

   /* "usr_rutafoto": "media/usuario/12_20191128130512.jpeg",
    "usr_email": "miguel1@hola.com.mx",
    "usr_id": "25",
    "usr_apm": "ramirez",
    "usr_nombre": "miguel",
    "usr_app": "ramirez"*/

    @GET("DataWS?ApiCall=recoveryPSw")
    fun recupearContrasenia(@Query ("email") email:String):Call<ResponseContrasenia>


    @GET("DataWS?ApiCall=getProductos")
    fun getProductos(): Call<ResponseProductos>


    //Acualizar fin foto
    //35.155.161.8:8080/WSExample/DataWS?ApiCall=updateUsr
    // &usrid=1 &email=miguel@hola.com &nip=123 &nombre=uanito &app=flores &apm=

    @GET("DataWS?ApiCall=updateUsr")
    fun updateUser(@Query("usrid") updateUsrid:String,
                   @Query("email") updateEmail:String,
                   @Query("nip") updateNip:String,
                   @Query("nombre") updateName:String,
                   @Query("app") updateApp:String,
                   @Query("apm") updateApm:String):Call<ResponseUpdate>

    //http://35.155.161.8:8080/WSExample/DataWS?ApiCall=setProdOrder&idproducto=2
    @GET("DataWS?ApiCall=setProdOrder")
    fun getOrden(@Query("id_producto")idProducto:String):Call<ResponseOrder>


    //Servicio de tipo multipart
    //MultipartBody para cada dato a enviar se necesita un MultipartBody, y se optiene la respuesta ServerResponse
    @Multipart
    @POST("DataWS")
    fun registroPerfil(@Part partMap: List<MultipartBody.Part> ): Call<ServerResponse>


}