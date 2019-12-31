package com.example.congnitusfirma.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseProductos(@SerializedName("productos")@Expose var product:List<ProductosResponse>
    )



