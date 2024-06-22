package com.example.shoppinglistapp

import kotlinx.coroutines.flow.Flow

class ShoppingRepository(private val ShoppingDao: ShoppingDao) {
    suspend fun addItem(item: ShoppingItem) {
        ShoppingDao.addItem(item)
    }

    fun getAllItems(): Flow<List<ShoppingItem>> {
        return ShoppingDao.getAllItems()
    }

    fun getItemById(id:Long) : Flow<ShoppingItem> {
        return ShoppingDao.getItemById(id)
    }

    suspend fun updateItem(item: ShoppingItem){
        ShoppingDao.updateItem(item)
    }

    suspend fun deleteItem(item: ShoppingItem) {
        ShoppingDao.deleteItem(item)
    }
}
