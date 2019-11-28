package com.example.congnitusfirma.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.congnitusfirma.R
import com.example.congnitusfirma.dao.APIService
import com.example.congnitusfirma.model.ResponseRegistro
import com.example.congnitusfirma.model.UsuariosResponse
import com.example.congnitusfirma.utilities.Utils
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistroActivity : AppCompatActivity() {

    lateinit var UsrEmail: String
    lateinit var UsrNombre: String
    lateinit var UsrApellidoP: String
    lateinit var UsrApellidoM:String
    lateinit var UsrContraseña:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        supportActionBar?.hide()

        val imgMenu = findViewById<ImageView>(R.id.imgMenuRegistro)
        val tvSesion = findViewById<TextView>(R.id.tVSesion)
        val email = findViewById<EditText>(R.id.eTCorreoR)
        val nombre = findViewById<EditText>(R.id.eTNombreR)
        val apellidoP = findViewById<EditText>(R.id.eTApellidoPaternoR)
        val apellidoM = findViewById<EditText>(R.id.eTApellidoMaternoR)
        val contrasenia = findViewById<EditText>(R.id.eTContraseñaR)
        val contraseniaRepetir = findViewById<EditText>(R.id.eTContraseñaRepetirR)
        val btnResitro = findViewById<Button>(R.id.btnRegistro)


        //evento menu
        imgMenu.setOnClickListener {
            startActivity(Intent(this@RegistroActivity, MainActivity::class.java))
            finish()
        }

        //evento menu
        tvSesion.setOnClickListener {
            startActivity(Intent(this@RegistroActivity, MainActivity::class.java))
            finish()
        }

        //RegistroUsuario
        btnResitro.setOnClickListener {
            if (email.text.isEmpty() || nombre.text.isEmpty() || apellidoP.text.isEmpty() ||
                apellidoM.text.isEmpty() || contrasenia.text.isEmpty() || contraseniaRepetir.text.isEmpty()){
                Toast.makeText(
                    this,
                    "Verifique que los campos no esten vacios",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
              if (Utils.isEmailValid(""+ email.text)){
                  Log.d("TAG","Evento 1")
                  if(contrasenia.text.toString().equals(contraseniaRepetir.text.toString())){
                      doAsync {
                          val call = registroRetrofi().create(APIService::class.java).registroUser(
                              ""+email.text.toString(),""+nombre.text.toString(),
                              ""+apellidoP.text.toString(),""+apellidoM.text.toString(),""+contrasenia.text.toString()).execute()
                          val respuesta = call.body() as ResponseRegistro
                          Log.i("Valor", "respuesta--- "+respuesta.valido)
                          uiThread{
                              if(respuesta.valido == "1"){
                                  val usrIds = respuesta.registroU.usrid
                                  val usrNombre = respuesta.registroU.usrNombre

                                  val ms = respuesta.mensaje

                                  Log.d("UserPreferenses ", usrIds+ " - "+ usrNombre )

                                  val sharedPreferences= getSharedPreferences("my_aplicacion_firma", Context.MODE_PRIVATE)
                                  var editor = sharedPreferences.edit()
                                  editor.putString("usr_id", usrIds)
                                  editor.putString("usr_name", usrNombre)
                                  editor.commit()

                                  val intent = Intent(this@RegistroActivity, MenuActivity::class.java)
                                  intent.putExtra("usr_id", usrIds)
                                  intent.putExtra("usr_nombre", usrNombre)
                                  intent.putExtra("MS", ms)
                                  startActivity(intent)
                              }else{
                                  showErrorDialog()
                                  email.setText("")
                                  nombre.setText("")
                                  apellidoP.setText("")
                                  apellidoM.setText("")
                                  contrasenia.setText("")
                                  contraseniaRepetir.setText("")
                              }
                          }
                      }
                  }else{
                      Toast.makeText(
                          this,
                          "Las contraseñas  son incorrectas",
                          Toast.LENGTH_SHORT
                      ).show()
                  }
              }else{
                  Toast.makeText(
                      this,
                      "El correo no cumple con el formato correcto",
                      Toast.LENGTH_SHORT
                  ).show()
              }
            }
        }
    }



    private fun registroRetrofi(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://35.155.161.8:8080/WSExample/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun showErrorDialog(){
        alert ( "Ha ocurrido un error, al registrarse inténtelo de nuevo." ){
            yesButton { "Si" }
        }.show()
    }

}
