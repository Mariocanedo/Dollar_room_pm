package com.example.dollar_room_pm.Model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface DollarTransactionDao {



    @Insert
    suspend fun insertTransaction(transationDao: DollarTrasanction)
    
    @Query("SELECT * FROM dolar_transacctions ORDER BY timestamp DESC")
    suspend fun getAllTransaction(): List<DollarTrasanction>
}