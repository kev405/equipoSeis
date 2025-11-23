package com.univalle.inventoryapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.univalle.inventoryapp.model.Inventory
import com.univalle.inventoryapp.utils.Constants.NAME_BD

@Database(entities = [Inventory::class], version = 1)
abstract class InventoryDB : RoomDatabase() {

    abstract fun inventoryDao(): InventoryDao
}