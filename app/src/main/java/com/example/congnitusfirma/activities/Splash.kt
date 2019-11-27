package com.example.congnitusfirma.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.congnitusfirma.R

class Splash : AppCompatActivity(), Runnable {

    private lateinit var handler:Handler;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        simulaCarga()

        supportActionBar?.hide()

    }

    private fun simulaCarga(){
        handler =  Handler()
        handler.postDelayed(this@Splash, 3000)
    }

    override fun run() {

        //validar si existe sesion
        val sharedPreferences= getSharedPreferences("my_aplicacion_firma", Context.MODE_PRIVATE)

        val usrId = sharedPreferences.getString("usr_id", "")
        Log.d("--->", usrId)

        //validacion
        if(!usrId.equals("")){
            // si hay sesio
            val intent = Intent(this@Splash, MenuActivity::class.java)
            intent.putExtra("nombre_usr", usrId)
            startActivity(intent)
            finish()
        }else{
            //no hay sesion
            val intent = Intent(this@Splash, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}
