package pt.nunopeixoto.wtest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.nunopeixoto.wtest.db.dao.PostalCodeDao
import pt.nunopeixoto.wtest.db.entity.PostalCode

private const val DB_NAME = "wtest_database"

@Database(entities = [(PostalCode::class)], version = 1)
abstract class WTestDatabase : RoomDatabase() {

    abstract fun postalCodeDao(): PostalCodeDao

    companion object {
        fun create(context: Context): WTestDatabase {
            return Room.databaseBuilder(
                context,
                WTestDatabase::class.java,
                DB_NAME
            ).build()
        }
    }
}