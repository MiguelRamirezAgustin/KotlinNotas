package com.example.congnitusfirma.model

import com.google.gson.annotations.SerializedName

class ResponseUpdateImg (@SerializedName("valido") var valido:String,
                         @SerializedName("mensaje") var mensaje:String,
                         @SerializedName("registro")val update:UserModelUpdate)


/*
* {
    "valido": "1",
    "mensaje": "Bienvenido",
    "registro": {
        "usr_rutafoto": "media/usuario/162_20191202180013.jpg",
        "usr_email": "aa",
        "usr_id": "162",
        "usr_apm": "aa",
        "usr_nombre": "aa",
        "usr_app": "aa"
    }
}
* */