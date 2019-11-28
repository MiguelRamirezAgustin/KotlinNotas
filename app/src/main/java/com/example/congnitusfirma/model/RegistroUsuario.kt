package com.example.congnitusfirma.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegistroUsuario (@SerializedName("usr_email") val usrEmail:String,
                       @SerializedName("usr_id") val usrid:String,
                       @SerializedName("usr_apm") val usrApm:String,
                       @SerializedName("usr_nombre") val usrNombre:String,
                       @SerializedName("usr_app") val usrApp:String
)

/* respuesta de registro
* {
    "valido": "1",
    "mensaje": "Bienvenido",
    "registro": {
        "usr_email": "miguel@hola.com.mx",
        "usr_id": "11",
        "usr_apm": "ramirez",
        "usr_nombre": "miguel",
        "usr_app": "ramirez"
    }
}
* */