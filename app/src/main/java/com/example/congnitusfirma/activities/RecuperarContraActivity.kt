package com.example.congnitusfirma.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.congnitusfirma.R

class RecuperarContraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contra)
        supportActionBar?.hide()


        val imgMenu = findViewById<ImageView>(R.id.imgMenu)
        val tVSesion = findViewById<TextView>(R.id.tVIniciarSesion)
        val tvRegistro = findViewById<TextView>(R.id.tVRegistro)


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

    }
}
