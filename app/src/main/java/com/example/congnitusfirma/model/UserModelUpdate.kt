package com.example.congnitusfirma.model

import com.google.gson.annotations.SerializedName

class UserModelUpdate (@SerializedName("usr_rutafoto") val usrImg:String,
                        @SerializedName("usr_email") val usrEmail:String,
                        @SerializedName("usr_id") val usrid:String,
                        @SerializedName("usr_apm") val usrApm:String,
                        @SerializedName("usr_nombre") val usrNombre:String,
                        @SerializedName("usr_app") val usrApp:String)