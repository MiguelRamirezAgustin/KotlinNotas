package com.example.congnitusfirma.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.congnitusfirma.R

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        supportActionBar?.hide()

        val LiReportes = findViewById<LinearLayout>(R.id.LisReportes)
        val LiFirma = findViewById<LinearLayout>(R.id.LiFirma)
        val btnSesion = findViewById<Button>(R.id.btnSesion)
        val nombre = findViewById<TextView>(R.id.tVNombreM)
        val dataParam = intent.getStringExtra("nombre_usr")


        //Evento de reportes
        LiReportes.setOnClickListener {
            val intent = Intent(this, ReportesActivity::class.java)
            startActivity(intent)
        }

        //cerrar sesion
        btnSesion.setOnClickListener {
            val builder = AlertDialog.Builder(this@MenuActivity)
            builder.setTitle("Alerta")
            builder.setMessage("Cerrar sesion ?")
            builder.setPositiveButton("No"){dialog, which ->

            }
            builder.setNegativeButton("Si"){dialog, which ->
                val shareActionProvider = getSharedPreferences("my_aplicacion_firma", Context.MODE_PRIVATE)
                var editor = shareActionProvider.edit()
                editor.putString("usr_id", "")
                editor.commit()
                val intent =  Intent(this@MenuActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }


        //Evento firma
        LiFirma.setOnClickListener {
            startActivity(Intent(this, FirmaActivity::class.java))
            finish()
        }


    }
}
