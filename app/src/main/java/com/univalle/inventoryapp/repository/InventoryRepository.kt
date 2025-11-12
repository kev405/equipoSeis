package com.univalle.inventoryapp.repository

import android.content.Context
import com.univalle.inventoryapp.data.InventoryDB
import com.univalle.inventoryapp.data.InventoryDao
import com.univalle.inventoryapp.model.Inventory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InventoryRepository (val context: Context){
    private var inventoryDao: InventoryDao = InventoryDB.getDatabase(context).inventoryDao()

    suspend fun getListInventory():MutableList<Inventory>{
        return withContext(Dispatchers.IO){
            inventoryDao.getListInventory()
        }
    }

    suspend fun insertProduct(inventory: Inventory){
        withContext(Dispatchers.IO){
            inventoryDao.insertProduct(inventory)
        }
    }

    suspend fun delete(inventory: Inventory){
        withContext(Dispatchers.IO){
            inventoryDao.delete(inventory)
        }
    }

    suspend fun update(inventory: Inventory){
        withContext(Dispatchers.IO){
            inventoryDao.update(inventory)
        }
    }

    suspend fun getTotalInventoryValue(): Double {
        return inventoryDao.getTotalInventoryValue()
    }

}