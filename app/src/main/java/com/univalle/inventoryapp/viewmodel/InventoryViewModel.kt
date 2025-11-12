package com.univalle.inventoryapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.univalle.inventoryapp.model.Inventory
import com.univalle.inventoryapp.repository.InventoryRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InventoryViewModel(application: Application) : AndroidViewModel(application) {
    val context = getApplication<Application>()
    private val inventoryRepository = InventoryRepository(context)

    private val _listInventory = MutableLiveData<MutableList<Inventory>>()
    val listInventory: LiveData<MutableList<Inventory>> get() = _listInventory

    private val _progressState = MutableLiveData(false)
    val progressState: LiveData<Boolean> = _progressState


    fun getListInventory() {
        viewModelScope.launch {
            _progressState.value = true
            try {
                _listInventory.value = inventoryRepository.getListInventory()
                _progressState.value = false
            } catch (e: Exception) {
                _progressState.value = false
            }

        }
    }

    fun insertProduct(inventory: Inventory) {
        viewModelScope.launch {
            try {
                inventoryRepository.insertProduct(inventory)
                getListInventory()
            } catch (e: Exception) {

            }
        }
    }

    fun deleteInventory(inventory: Inventory) {
        viewModelScope.launch {
            _progressState.value = true
            try {
                inventoryRepository.delete(inventory)
                _progressState.value = false
            } catch (e: Exception) {
                _progressState.value = false
            }

        }
    }

    fun updateInventory(inventory: Inventory) {
        viewModelScope.launch {
            _progressState.value = true
            try {
                inventoryRepository.update(inventory)
                _progressState.value = false
            } catch (e: Exception) {
                _progressState.value = false
            }
        }
    }

    fun totalProducto(price: Double, quantity: Int): Double {
        val total = price * quantity
        return total
    }

    suspend fun getTotalInventoryValue(): Double {
        return withContext(Dispatchers.IO) {
            inventoryRepository.getTotalInventoryValue()
        }
    }

}