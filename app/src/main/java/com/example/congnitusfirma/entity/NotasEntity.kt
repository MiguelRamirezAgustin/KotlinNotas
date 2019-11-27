package com.example.congnitusfirma.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notas_entity")
data class NotasEntity (
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    var name:String = "",
    var fecha: String= "00/00/0000",
    var isDone:Boolean = false
)