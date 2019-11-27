package com.example.congnitusfirma.dao

import androidx.room.*
import com.example.congnitusfirma.entity.NotasEntity

@Dao
interface NotasDao {
    //donde se colocan los query

    @Query("SELECT * FROM notas_entity")
    fun getAllTasks(): MutableList<NotasEntity>

    @Insert
    fun addTask(taskEntity : NotasEntity):Long

    // select con un parametro id
    @Query("SELECT * FROM notas_entity where id like :id ")
    fun getTaskById(id: Long): NotasEntity

    @Delete
    fun deleteTask(taskEntity: NotasEntity):Int

    @Update
    fun updateTask(taskEntity: NotasEntity):Int


}
