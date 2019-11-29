package com.example.congnitusfirma.activities

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.congnitusfirma.R
import com.example.congnitusfirma.dao.APIService
import com.example.congnitusfirma.model.ResponseOrder
import org.jetbrains.anko.AlertDialogBuilder
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetalleProductoActivity : AppCompatActivity() {
    var strId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_producto)
        supportActionBar?.hide()


        val titleProductoParamas = intent.getStringExtra("titleProducto")
        val nUrl = intent.getStringExtra("img_url")
        val detalleProductoParmas = intent.getStringExtra("detalle_Producto")
        val detallePrecioParams = intent.getStringExtra("detalle_precio")
        val idProducto = intent.getStringExtra("id_producto")

        Log.d("Id_Producto 1", idProducto)

        val imgProductoDetalle = findViewById<ImageView>(R.id.imgProductoDetalle)
        val tvTituloProductoDetalle = findViewById<TextView>(R.id.tVProductoDetalleTitulo)
        val tvDetalleProducto = findViewById<TextView>(R.id.tVDetalleProducto)
        val tvDetallePrecio = findViewById<TextView>(R.id.tVProductoDetallePrecio)
        val tvCompras = findViewById<ImageView>(R.id.btnCompras)

        //Leer imagen
        val requestManager = Glide.with(this)
        val requestBuilder = requestManager.load("http://35.155.161.8:8080/WSExample/$nUrl")
        requestBuilder.into(imgProductoDetalle)

        tvTituloProductoDetalle.setText(titleProductoParamas)
        tvDetalleProducto.setText(detalleProductoParmas)
        tvDetallePrecio.setText("$ "+detallePrecioParams +" MXN")
        strId = idProducto
        Log.d("Id_Producto", strId)

    tvCompras.setOnClickListener{
        doAsync{
                val call = orderRetrofit().create(APIService::class.java).getOrden(""+strId).execute()
                val respuesta = call.body() as ResponseOrder
                Log.i("TAG", "Respuesta"+ respuesta.valido)
                    uiThread {
                        if (respuesta.valido == "1"){
                            val alerBuilder = AlertDialog.Builder(this@DetalleProductoActivity)
                            alerBuilder.setTitle("Alerta")
                            alerBuilder.setMessage("El ide de la orden es: "+respuesta.respuesta.orden)
                            alerBuilder.setPositiveButton("Si"){dialog, which ->
                            }
                            val dialog: AlertDialog = alerBuilder.create()
                            dialog.show()
                        }else if (respuesta.valido == "0"){
                            showErrorDialog()
                        }
                    }
                }
            }

    }




    private fun orderRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("http://35.155.161.8:8080/WSExample/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun showErrorDialog() {
        alert("Ha ocurrido un error, al agregar producto a la compra.") {
            yesButton { }
        }.show()
    }
}
