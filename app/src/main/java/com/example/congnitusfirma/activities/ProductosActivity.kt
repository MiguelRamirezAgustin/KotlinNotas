package com.example.congnitusfirma.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.congnitusfirma.R
import com.example.congnitusfirma.dao.APIService
import com.example.congnitusfirma.model.ProductosResponse
import com.example.congnitusfirma.model.ResponseProducto
import com.example.congnitusfirma.model.UsuariosResponse
import kotlinx.android.synthetic.main.activity_productos.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)
        supportActionBar?.hide()


        btnProducto.setOnClickListener {

            doAsync{
                Toast.makeText(this@ProductosActivity, "Evento", Toast.LENGTH_SHORT).show()
                val call = getProductRetrofit().create(APIService::class.java).getProductos().execute()
                val respuesta = call.body() as ResponseProducto
                Log.i("TAG", "Respuesta-- "+ respuesta.productos.prodTit)
            }
        }
    }



    //http://35.155.161.8:8080/WSExample/DataWS?ApiCall=getProductos
    private fun getProductRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("http://35.155.161.8:8080/WSExample/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun showErrorDialog() {
        alert("Ha ocurrido un error, inténtelo de nuevo.") {
            yesButton { }
        }.show()
    }
}
