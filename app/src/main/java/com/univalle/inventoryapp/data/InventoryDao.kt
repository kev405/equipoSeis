package com.univalle.inventoryapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.univalle.inventoryapp.model.Inventory
import kotlinx.coroutines.flow.Flow


@Dao
interface InventoryDao {

    @Query("SELECT * FROM Inventory")
    suspend fun getListInventory(): MutableList<Inventory>

    @Insert
    suspend fun insertProduct(inventory: Inventory): Long

    @Update
    suspend fun update(inventory: Inventory): Int

    @Delete
    suspend fun delete(inventory: Inventory): Int

    @Query("SELECT COALESCE(SUM(price * quantity), 0.0) FROM Inventory")
    suspend fun getTotalInventoryValue(): Double

}