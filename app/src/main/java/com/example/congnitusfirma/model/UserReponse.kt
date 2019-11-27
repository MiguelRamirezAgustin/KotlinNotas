package com.example.congnitusfirma.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserReponse (@SerializedName("usr_id")@Expose var usrId:String,
                   @SerializedName("usr_nombre")@Expose var usrNombre:String
)