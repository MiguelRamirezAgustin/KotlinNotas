package com.example.congnitusfirma.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.congnitusfirma.R
import com.example.congnitusfirma.dao.APIService
import com.example.congnitusfirma.model.UsuariosResponse
import com.example.congnitusfirma.utilities.Utils
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    lateinit var strUsr: String
    lateinit var strNip: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val RecuperarContraseña = findViewById<TextView>(R.id.tVRecuperarContraseña)
        val tvRegistro = findViewById<TextView>(R.id.tVRegistro)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        val tvUsuario = findViewById<EditText>(R.id.eTUsuario)
        val tvContraseña = findViewById<EditText>(R.id.eTContraseña)


        //Evento para login
        btnIngresar.setOnClickListener {
            if (tvUsuario.text.isEmpty() || tvContraseña.text.isEmpty()){
                Toast.makeText(
                    this,
                    "Los campos no pueden estar vacios",
                    Toast.LENGTH_SHORT
                ).show()
            }else {
                if (Utils.isEmailValid(""+ tvUsuario.text)){
                    strUsr = tvUsuario.text.toString()
                    strNip = tvContraseña.text.toString()
                    LoginUser(strUsr , strNip )
                }else {
                    Toast.makeText(
                        this,
                        "La correo no cumple con el formato",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // evento para recuperar contraseña
        RecuperarContraseña.setOnClickListener {
            startActivity(Intent(this@MainActivity, RecuperarContraActivity::class.java))
            finish()
        }

        //evento para ir a registro
        tvRegistro.setOnClickListener {
            startActivity(Intent(this@MainActivity, RegistroActivity::class.java))
            finish()
        }
    }

    private fun LoginUser (strUsr:String, strNip:String){
        Log.d("TAG", "Login___ "+ strUsr + strNip )
        doAsync{
            val call = loginRetrofit().create(APIService::class.java).loginPost(""+strUsr ,""+strNip ).execute()
            val respuesta = call.body() as UsuariosResponse
            uiThread {
                if(respuesta.validoLogin == "1") {
                    /* response de login
                    *
                    * {
                    "valido": "1",
                    "usuario": {
                        "usr_rutafoto": "media/usuario/12_20191128130512.jpeg",
                        "usr_email": "miguel@hola.com",
                        "usr_id": "15",
                        "usr_apm": "AgustÃ­n",
                        "usr_nombre": "Miguel",
                        "usr_app": "Ramirez"
                      }
                    }
                    * */

                    val usrIds = respuesta.usuarioLogin?.usrId
                    val usrNombre = respuesta.usuarioLogin?.usrNombre
                    val usrEmail = respuesta.usuarioLogin?.usrEmail
                    val usrImg = respuesta.usuarioLogin?.usrRutaFoto
                    val usrApp = respuesta.usuarioLogin?.usrApp
                    val usrApm = respuesta.usuarioLogin?.usrApm

                    Log.d("UserPreferenses ", usrIds+ " "+ usrNombre+ " "+usrEmail+" "+usrApp+" "+usrApm+ " "+usrImg   )

                    //Guardar datos de sesion
                    val sharedPreferences= getSharedPreferences("my_aplicacion_firma", Context.MODE_PRIVATE)
                    var editor = sharedPreferences.edit()
                    editor.putString("usr_id", usrIds)
                    editor.putString("usr_email", usrEmail)
                    editor.putString("usr_App", usrApp)
                    editor.putString("usr_Apm", usrApm)
                    editor.putString("usr_name", usrNombre)
                    editor.putString("usr_Img", usrImg)
                    editor.commit()

                    val intent = Intent(applicationContext, MenuActivity::class.java)
                    intent.putExtra("usr_id", usrIds)
                    intent.putExtra("usr_nombre", usrNombre)
                     startActivity(intent)
                    Log.d("ResulLogin", respuesta.toString())
                }else{
                    showErrorDialog()
                }
             }
         }
    }

    private fun loginRetrofit(): Retrofit {
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
