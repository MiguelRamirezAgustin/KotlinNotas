package com.example.congnitusfirma.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.congnitusfirma.R
import com.example.congnitusfirma.adapter.ProductoAdpater
import com.example.congnitusfirma.dao.APIService
import com.example.congnitusfirma.model.ResponseProductos
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductosActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)
        supportActionBar?.hide()


        getProdutos()
    }


    private fun getProdutos(){
            Toast.makeText(this@ProductosActivity, "Evento", Toast.LENGTH_SHORT).show()
            doAsync{
                val call = getProductRetrofit().create(APIService::class.java).getProductos().execute()
                val respuesta = call.body() as ResponseProductos
                uiThread {
                    linearLayoutManager = LinearLayoutManager(applicationContext)
                    val rvProductos = findViewById<RecyclerView>(R.id.rvProducto)
                    rvProductos.layoutManager = linearLayoutManager
                    rvProductos.adapter = ProductoAdpater(this@ProductosActivity, respuesta.product)
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
