package com.example.congnitusfirma.activities

import android.app.Application
import androidx.room.Room
import com.example.congnitusfirma.database.NotasDatabase

class MisNotasApp: Application() {

    // se agrega al manifes android:name=".activities.MisNotasApp"

    companion object {
        // para poder usar mas tarde
        lateinit var database: NotasDatabase
    }

    override fun onCreate() {
        super.onCreate()
        //cuando se crea la base de datos con el nombre de la base de datos que se va utilizar
        database =  Room.databaseBuilder(this, NotasDatabase::class.java, "notas-db").build()
    }
}