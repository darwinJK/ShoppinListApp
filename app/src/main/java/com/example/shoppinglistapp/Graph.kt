package com.example.shoppinglistapp

import android.content.Context
import androidx.room.Room

object Graph {
    lateinit var database: ShoppingDatabase

    val ShoppingRepository by lazy {
        ShoppingRepository(ShoppingDao = database.shoppingDao())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(
            context,
            ShoppingDatabase::class.java,
            "ShoppingList.db"
        ).build()
    }
}