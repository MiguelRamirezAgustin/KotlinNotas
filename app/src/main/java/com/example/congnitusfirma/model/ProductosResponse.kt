package com.example.congnitusfirma.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductosResponse (@SerializedName("prod_desc")@Expose  val prodDes: String,
                         @SerializedName("prod_titulo")@Expose val prodTit: String,
                         @SerializedName("prod_precio")@Expose  val prodPre: String,
                         @SerializedName("prod_img")@Expose  val prodImg: String,
                         @SerializedName("prod_id")@Expose  val prodId: String)