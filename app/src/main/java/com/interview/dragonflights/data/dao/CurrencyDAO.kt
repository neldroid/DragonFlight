package com.interview.dragonflights.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.interview.dragonflights.data.model.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDAO {

    @Query("SELECT * FROM currency WHERE `from` = :fromCurrency AND `to` = :toCurrency")
    fun getCurrencyExchange(fromCurrency: String, toCurrency: String): Flow<Currency>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currency: Currency)

}