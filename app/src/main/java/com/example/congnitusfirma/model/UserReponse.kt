package com.example.congnitusfirma.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserReponse (@SerializedName("usr_rutafoto") var usrRutaFoto:String,
                   @SerializedName("usr_email") var usrEmail:String,
                   @SerializedName("usr_id") var usrId:String,
                   @SerializedName("usr_apm") var usrApm:String,
                   @SerializedName("usr_nombre") var usrNombre:String,
                   @SerializedName("usr_app") var usrApp:String
)

/* response de login
* "usr_rutafoto": "media/usuario/12_20191128130512.jpeg",
        "usr_email": "miguel@hola.com",
        "usr_id": "15",
        "usr_apm": "AgustÃ­n",
        "usr_nombre": "Miguel",
        "usr_app": "Ramirez"
* */