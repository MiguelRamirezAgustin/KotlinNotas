package com.example.congnitusfirma.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.congnitusfirma.dao.NotasDao
import com.example.congnitusfirma.entity.NotasEntity


/*Database referencia en entities y una version para controlar la base de datos para subir a tienda
TasEntity se usa las veces que tenga una table por cada tabla
TaskDao se usa las veces que se use ua entity
database se una una sola vez
*/
@Database(entities = arrayOf(NotasEntity::class), version = 1)
abstract class NotasDatabase : RoomDatabase() {
    abstract fun notaskDao(): NotasDao
}