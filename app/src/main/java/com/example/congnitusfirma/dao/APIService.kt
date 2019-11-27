package com.example.congnitusfirma.dao

import com.example.congnitusfirma.model.ResponseRegistro
import com.example.congnitusfirma.model.UsuariosResponse
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
        @Query ("apm ") apellidoMR:String,
        @Query ("nip") contraR:String
    ): Call<ResponseRegistro>
}