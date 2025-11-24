package com.univalle.inventoryapp.repository

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.univalle.inventoryapp.data.InventoryDB
import com.univalle.inventoryapp.data.InventoryDao
import com.univalle.inventoryapp.model.Inventory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InventoryRepository @Inject constructor(
    private val inventoryDao: InventoryDao
){
    private val db = FirebaseFirestore.getInstance()

    suspend fun getListInventory():MutableList<Inventory>{
        return withContext(Dispatchers.IO){
            suspendCancellableCoroutine { cont ->
                db.collection("inventory").get()
                    .addOnSuccessListener { result ->
                        try {
                            val inventoryList = mutableListOf<Inventory>()
                            for (document in result) {
                                val inventory = Inventory(
                                    id = document.getLong("id")?.toInt() ?: 0,
                                    name = document.getString("name") ?: "",
                                    price = document.getDouble("price") ?: 0.0,
                                    quantity = document.getLong("quantity")?.toInt() ?: 0
                                )
                                inventoryList.add(inventory)
                            }
                            cont.resumeWith(Result.success(inventoryList))
                        } catch (e: Exception) {
                            cont.resumeWith(Result.failure(e))
                        }
                    }
                    .addOnFailureListener { e ->
                        cont.resumeWith(Result.failure(e))
                    }
            }
        }
    }


    suspend fun insertProduct(inventory: Inventory){
        withContext(Dispatchers.IO){
            inventoryDao.insertProduct(inventory)
        }
    }

    suspend fun delete(inventory: Inventory){
        return withContext(Dispatchers.IO){
            suspendCancellableCoroutine { cont ->
                db.collection("inventory")
                    .document(inventory.id.toString())
                    .delete()
                    .addOnSuccessListener {
                        cont.resumeWith(Result.success(Unit))
                    }
                    .addOnFailureListener { e ->
                        cont.resumeWith(Result.failure(e))
                    }
            }
        }
    }

    suspend fun update(inventory: Inventory){
        return withContext(Dispatchers.IO){
            suspendCancellableCoroutine { cont ->
                db.collection("inventory")
                    .document(inventory.id.toString())
                    .set(
                        hashMapOf(
                            "id" to inventory.id,
                            "name" to inventory.name,
                            "price" to inventory.price,
                            "quantity" to inventory.quantity
                        )
                    )
                    .addOnSuccessListener {
                        cont.resumeWith(Result.success(Unit))
                    }
                    .addOnFailureListener { e ->
                        cont.resumeWith(Result.failure(e))
                    }
            }
        }
    }

    suspend fun getTotalInventoryValue(): Double {
        return withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { cont ->
                db.collection("inventory").get()
                    .addOnSuccessListener { result ->
                        try {
                            var totalValue = 0.0
                            for (document in result) {
                                val price = document.getDouble("price") ?: 0.0
                                val quantity = document.getLong("quantity")?.toInt() ?: 0
                                totalValue += price * quantity
                            }
                            cont.resumeWith(Result.success(totalValue))
                        } catch (e: Exception) {
                            cont.resumeWith(Result.failure(e))
                        }
                    }
                    .addOnFailureListener { e ->
                        cont.resumeWith(Result.failure(e))
                    }
            }
        }
    }

}