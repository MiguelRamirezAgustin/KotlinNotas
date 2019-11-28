package com.example.congnitusfirma.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseProducto (@SerializedName("productos")@Expose val  productos:ProductosResponse)