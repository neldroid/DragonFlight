package com.interview.dragonflights.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.interview.dragonflights.data.model.Currency

@Database(entities = [Currency::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun currencyDao(): CurrencyDAO
}