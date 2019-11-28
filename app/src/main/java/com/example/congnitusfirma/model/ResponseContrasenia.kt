package com.example.congnitusfirma.model

import com.google.gson.annotations.SerializedName

class ResponseContrasenia (@SerializedName("mesaje") var mensaje:String,
                           @SerializedName("valido") var valido:String)