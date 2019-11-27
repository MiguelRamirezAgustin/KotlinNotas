package com.example.congnitusfirma.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UsuariosResponse (@SerializedName("valido") var validoLogin:String,
                             @SerializedName("usuario")@Expose val  usuarioLogin:UserReponse?
)