package com.example.congnitusfirma.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseRegistro (@SerializedName("valido") var valido:String,
                             @SerializedName("mensaje") var mensaje:String,
                             @SerializedName("registro")@Expose val registroU:RegistroUsuario
)

