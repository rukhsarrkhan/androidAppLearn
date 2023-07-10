package com.example.secondproject

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GadgetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGadget(gadget: Gadget)

//    @Query("SELECT * FROM gadget ORDER BY author DESC")
    @Query("SELECT * FROM gadget")

    fun getGadgets(): Flow<List<Gadget>>

    @Update
    suspend fun updateGadget(gadget: Gadget)

    @Delete
    suspend fun deleteGadget(gadget: Gadget)

    @Query("DELETE FROM gadget WHERE id=:gadgetId")
    fun deleteGadgetById(gadgetId: String)

}
