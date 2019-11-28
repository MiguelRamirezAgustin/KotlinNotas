package com.example.congnitusfirma.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.example.congnitusfirma.R
import kotlinx.android.synthetic.main.activity_perfil.*

class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        supportActionBar?.hide()

        val imgBakc = findViewById<ImageView>(R.id.imgBackPerfil)

        //evento regreso a menu
        imgBakc.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }

        //Evento actualizar
        btnActualizarP.setOnClickListener {
            Toast.makeText(this, "actualizar", Toast.LENGTH_SHORT).show()
        }

    }
}
