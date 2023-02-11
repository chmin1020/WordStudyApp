package com.example.wordpractice.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordDB: RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object{
        private var instance: WordDB? = null

        @Synchronized
        fun getInstance(context: Context): WordDB{
            return instance ?: run{
                //null -> 최초 DB 접근. 새로운 DB 객체를 생성하여 적용
                Room.databaseBuilder(context.applicationContext, WordDB::class.java, "WordDatabase")
                    .build()
                    .also { instance = it }
            }
        }
    }
}