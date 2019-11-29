package com.example.congnitusfirma.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseOrder (@SerializedName("valido") var valido:String,
                     @SerializedName("respuesta")@Expose var respuesta:OrdenResponse)


/*  Respuesta de la orden
* {
"valido": "1",
"respuesta": {
"orden": "1127150976"
}
}
* */