package com.example.congnitusfirma.model

import android.content.ClipData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject

class ResponseProductos(@SerializedName("productos")@Expose var product:List<ProductosResponse>
    )



