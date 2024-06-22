package com.example.shoppinglistapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract  class ShoppingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addItem(shoppingEntity:ShoppingItem)

    @Query("select * from `Shopping-table`")
    abstract fun getAllItems(): Flow<List<ShoppingItem>>

    @Update
    abstract suspend fun updateItem(shoppingEntity: ShoppingItem)

    @Delete
    abstract suspend fun deleteItem(shoppingEntity: ShoppingItem)

    @Query("SELECT * FROM `Shopping-table` WHERE id = :id")
    abstract fun getItemById(id: Long): Flow<ShoppingItem>

}