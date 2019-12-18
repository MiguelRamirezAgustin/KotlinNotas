package com.example.congnitusfirma.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductosResponse(
    @SerializedName("prod_desc") @Expose val prodDes: String,
    @SerializedName("prod_titulo") @Expose val prodTit: String,
    @SerializedName("prod_precio") @Expose val prodPre: String,
    @SerializedName("prod_img") @Expose val prodImg: String,
    @SerializedName("prod_id") @Expose val prodId: String
)


/* response
* {
"productos": [
{
"prod_desc": "Con hasta 22 horas de duración de la batería recargable y conectividad Bluetooth®, Hesh 3 Wireless está diseñado con un ajuste que aísla el ruido para una escucha cómoda durante todo el día. ",
"prod_titulo": "Audifonos Skullcandy",
"prod_precio": "1599.0",
"prod_img": "media/producto/producto1.jpg",
"prod_id": "1"
},
{
"prod_desc": "Diseñado para ofrecer una experiencia perfecta desde el principio. Estos altavoces con certificación THX® se han optimizado con unas especificaciones exactas para ofrecer con el máximo realismo la visión original",
"prod_titulo": "Bocinas Logitech Z623",
"prod_precio": "2398.0",
"prod_img": "media/producto/producto2.jpg",
"prod_id": "3"
},
{
"prod_desc": "ipo de Teclado: Inalámbrico con Mouse ,Diseño del Teclado: Estándar ,Interfaz: Receptor USB, Distribución de Teclado: Español",
"prod_titulo": "Teclado y Mouse Inalámbricos Logitech MK540",
"prod_precio": "768.0",
"prod_img": "media/producto/producto3.jpg",
"prod_id": "4"
},
{
"prod_desc": "De alta calidad, duradero y repelente al agua tela, Gran compartimento para portatil se ajusta hasta 39,6 cm portatil , Con bolsillos independientes para dispositivos",
"prod_titulo": "ZBag Mochila para laptop",
"prod_precio": "1305.0",
"prod_img": "media/producto/producto4.jpg",
"prod_id": "2"
}
]
}
* */