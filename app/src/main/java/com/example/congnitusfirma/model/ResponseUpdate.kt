package com.example.congnitusfirma.model

import com.google.gson.annotations.SerializedName

class ResponseUpdate (@SerializedName("valido") var valido:String,
                      @SerializedName("mensaje") var mensaje:String)