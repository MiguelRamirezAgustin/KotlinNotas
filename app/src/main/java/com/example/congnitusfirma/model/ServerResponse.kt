package com.example.congnitusfirma.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ServerResponse (  @SerializedName("mensaje") var mensaje: String? = null,
                        @SerializedName("valido") var valido: String? = null,
                        @SerializedName("registro") var user: UserModel)

/* Response registro
{
    "valido": "1",
    "mensaje": "Bienvenido",
    "registro": {
        "usr_rutafoto": "media/usuario/10_20191128125603.jpeg",
        "usr_email": "luis@hola.com.mx",
        "usr_id": "10",
        "usr_apm": "lopez",
        "usr_nombre": "test",
        "usr_app": "lópez"
    }
}
*/