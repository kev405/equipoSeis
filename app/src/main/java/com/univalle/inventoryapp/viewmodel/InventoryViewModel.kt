package com.univalle.inventoryapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.univalle.inventoryapp.model.Inventory
import com.univalle.inventoryapp.repository.InventoryRepository
import kotlinx.coroutines.launch

class InventoryViewModel(application: Application) : AndroidViewModel(application) {
    val context = getApplication<Application>()
    private val inventoryRepository = InventoryRepository(context)

    private val _listInventory = MutableLiveData<MutableList<Inventory>>()
    val listInventory: LiveData<MutableList<Inventory>> get() = _listInventory

    private val _progressState = MutableLiveData(false)
    val progresState: LiveData<Boolean> = _progressState


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

}