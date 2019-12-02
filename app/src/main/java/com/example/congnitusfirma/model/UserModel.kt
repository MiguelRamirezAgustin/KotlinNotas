package com.example.congnitusfirma.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserModel (@SerializedName("usr_id") var id:String,
                @SerializedName("usr_rutafoto") var ruta: String,
                @SerializedName("usr_email") var email: String,
                @SerializedName("usr_nip") var nip: String,
                @SerializedName("usr_nombre") var nombre: String,
                @SerializedName("usr_app") var app: String,
                @SerializedName("usr_apm") var apm: String)



/* Response registro
{
    "valido": "1",
    "mensaje": "Bienvenido",
    "registro": {
        "usr_rutafoto": "media/usuario/74_20191202113320.jpg",
        "usr_email": "agustin@holtmail.com",
        "usr_id": "74",
        "usr_apm": "ramirez",
        "usr_nombre": "agustin",
        "usr_app": "ramirez"
    }
}


@SerializedName("usr_id") var id:String,
                     @SerializedName("usr_rutafoto") var ruta: String,
                     @SerializedName("usr_email") var email: String,
                     @SerializedName("usr_nip") var nip: String,
                     @SerializedName("usr_nombre") var nombre: String,
                     @SerializedName("usr_app") var app: String,
                     @SerializedName("usr_apm") var apm: String
*/