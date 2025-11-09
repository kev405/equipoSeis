package com.univalle.inventoryapp.view.viewholder

import android.os.Bundle
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.univalle.inventoryapp.R
import com.univalle.inventoryapp.databinding.ItemInventoryBinding
import com.univalle.inventoryapp.model.Inventory
import com.univalle.inventoryapp.utils.PriceFormatter


class InventoryViewHolder (binding: ItemInventoryBinding, navController: NavController) :
    RecyclerView.ViewHolder(binding.root) {
    val bindingItem = binding
    val navController = navController

    fun setItemInventory(inventory: Inventory) {
        bindingItem.textViewName.text = inventory.name
        bindingItem.textViewItemId.text = "ID: ${inventory.id}"
        bindingItem.textViewPrice.text = "$ ${PriceFormatter.formatPrice(inventory.price)}"

        bindingItem.cardViewInventory.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("key", inventory)
            navController.navigate(R.id.action_homeInventoryFragment_to_itemDetailsFragment, bundle)
        }
    }
}