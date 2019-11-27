package com.example.congnitusfirma.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegistroUsuario (@SerializedName("usr_id") val usrid:String,
                       @SerializedName("usr_nombre") val usrNombre:String
)

