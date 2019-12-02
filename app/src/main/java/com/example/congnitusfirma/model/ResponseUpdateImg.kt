package com.example.congnitusfirma.model

import com.google.gson.annotations.SerializedName

class ResponseUpdateImg (@SerializedName("valido") var valido:String,
                         @SerializedName("mensaje") var mensaje:String,
                         @SerializedName("registro")val update:UserModelUpdate)