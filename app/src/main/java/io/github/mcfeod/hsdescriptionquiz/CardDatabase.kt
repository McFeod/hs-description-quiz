package io.github.mcfeod.hsdescriptionquiz

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Card::class], version = 1)
abstract class CardDatabase: RoomDatabase() {
    abstract fun cardDao(): IDatabaseRepository

    companion object : SingletonHolder<CardDatabase, Context>({
        Room.databaseBuilder(
            it.applicationContext,
            CardDatabase::class.java,
            "io.github.mcfeod.hsdescriptionquiz.db"
        ) .build()
    })
}
