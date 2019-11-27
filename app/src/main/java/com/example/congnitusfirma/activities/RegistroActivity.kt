package com.example.congnitusfirma.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.congnitusfirma.R

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        supportActionBar?.hide()

        val imgMenu = findViewById<ImageView>(R.id.imgMenuRegistro)
        val tvSesion = findViewById<TextView>(R.id.tVSesion)


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


    }
}
