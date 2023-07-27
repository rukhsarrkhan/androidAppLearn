package com.example.secondproject.model

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.secondproject.model.data.Gadget
import com.example.secondproject.model.interfaces.GadgetDao
import com.example.secondproject.view.FourthFragment

@Database(
    entities = [Gadget::class],
    version = 1,
    exportSchema = true
)
abstract class GadgetDatabase : RoomDatabase() {

    abstract fun gadgetDao(): GadgetDao

    companion object {

        @Volatile
        private var INSTANCE: GadgetDatabase? = null

        fun getDatabase(context: FourthFragment): GadgetDatabase {
// if the INSTANCE is not null, then return it,
// if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
// Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
// Return database.
            return INSTANCE!!
        }

        private fun buildDatabase(context: FourthFragment): GadgetDatabase {
            return Room.databaseBuilder(
                context.requireContext(),
                GadgetDatabase::class.java,
                "gadget_database"
            )
                .build()
        }
    }
}