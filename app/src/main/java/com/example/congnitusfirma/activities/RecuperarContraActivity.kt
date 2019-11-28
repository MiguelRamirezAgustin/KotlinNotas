package com.example.congnitusfirma.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.congnitusfirma.R
import com.example.congnitusfirma.dao.APIService
import com.example.congnitusfirma.model.ResponseContrasenia
import com.example.congnitusfirma.utilities.Utils
import org.jetbrains.anko.AlertDialogBuilder
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecuperarContraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contra)
        supportActionBar?.hide()


        val imgMenu = findViewById<ImageView>(R.id.imgMenu)
        val tVSesion = findViewById<TextView>(R.id.tVIniciarSesion)
        val tvRegistro = findViewById<TextView>(R.id.tVRegistro)
        val eTCorreo = findViewById<TextView>(R.id.eTRecuperarContrasenia)
        val btnRecuperarContrasenia = findViewById<Button>(R.id.btnRecuperarContrasenia)

        //evento ir a sesion
        imgMenu.setOnClickListener{
            startActivity(Intent(this@RecuperarContraActivity, MainActivity::class.java))
            finish()
        }


        //evento ir a sesion
        tVSesion.setOnClickListener {
            startActivity(Intent(this@RecuperarContraActivity, MainActivity::class.java))
            finish()
        }

        //evento registro
        tvRegistro.setOnClickListener {
            startActivity(Intent(this@RecuperarContraActivity, RegistroActivity::class.java))
            finish()
        }

        //recuperar contraseñia
        btnRecuperarContrasenia.setOnClickListener {
            if (eTCorreo.text.isEmpty()){
                Toast.makeText(
                    this,
                    "El correo es requerido",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                if (Utils.isEmailValid(""+eTCorreo.text)){
                    doAsync{
                        val call = recuperarContraseniaRetrofi().create(APIService::class.java).recupearContrasenia(""+eTCorreo.text.toString()).execute()
                        val respuesta = call.body() as ResponseContrasenia
                        Log.i("respuesta   ", respuesta.valido)
                        uiThread {
                            if (respuesta.valido == "1"){
                                val builderAler = AlertDialogBuilder(this@RecuperarContraActivity)
                                builderAler.title("Alerta")
                                builderAler.message(""+respuesta.mensaje)
                                builderAler.yesButton { "SI" }
                                builderAler.show()
                            }else if(respuesta.valido == "0"){
                                val builderAler = AlertDialogBuilder(this@RecuperarContraActivity)
                                builderAler.title("Alerta")
                                builderAler.message(""+respuesta.mensaje)
                                builderAler.yesButton { "SI" }
                                builderAler.show()
                            }else{
                                showErrorDialog()
                            }
                        }
                    }

                }else{
                    Toast.makeText(
                        this,
                        "El correo no cumple con el formato",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun recuperarContraseniaRetrofi(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://35.155.161.8:8080/WSExample/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun showErrorDialog(){
        alert ( "Ha ocurrido un error, inténtelo de nuevo." ){
            yesButton { "Si" }
        }.show()
    }
}
