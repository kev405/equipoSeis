package com.univalle.inventoryapp.view.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.univalle.inventoryapp.R
import com.univalle.inventoryapp.databinding.FragmentItemEditBinding
import com.univalle.inventoryapp.model.Inventory
import com.univalle.inventoryapp.viewmodel.InventoryViewModel

class ItemEditFragment : Fragment() {
    private lateinit var binding: FragmentItemEditBinding

    private val inventoryViewModel: InventoryViewModel by viewModels()
    private lateinit var receivedInventory: Inventory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemEditBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInventory()
        controladores()
        setupValidation()
    }

    private fun setupValidation() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateFields()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.editProductNameText.addTextChangedListener(textWatcher)
        binding.editProductPriceText.addTextChangedListener(textWatcher)
        binding.editProductQuantityText.addTextChangedListener(textWatcher)
    }

    private fun validateFields() {
        val name = binding.editProductNameText.text.toString().trim()
        val price = binding.editProductPriceText.text.toString().trim()
        val quantity = binding.editProductQuantityText.text.toString().trim()

        binding.buttonEdit.isEnabled = name.isNotEmpty() && price.isNotEmpty() && quantity.isNotEmpty()
        if (binding.buttonEdit.isEnabled) {
            binding.buttonEdit.typeface = Typeface.DEFAULT_BOLD
        } else {
            binding.buttonEdit.typeface = Typeface.DEFAULT
        }
    }


    private fun controladores(){
        binding.buttonEdit.setOnClickListener {
            updateInventory()
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_itemEditFragment_to_homeInventoryFragment)
        }
    }

    private fun dataInventory(){
        val receivedBundle = arguments
        receivedInventory = receivedBundle?.getSerializable("dataInventory") as Inventory
        binding.textProductIdHeader.text = receivedInventory.id.toString()
        binding.editProductNameText.setText(receivedInventory.name)
        binding.editProductPriceText.setText(receivedInventory.price.toString())
        binding.editProductQuantityText.setText(receivedInventory.quantity.toString())

        validateFields()
    }

    private fun updateInventory(){
        val name = binding.editProductNameText.text.toString()
        val price = binding.editProductPriceText.text.toString().toInt()
        val quantity = binding.editProductQuantityText.text.toString().toInt()
        val inventory = Inventory(receivedInventory.id, name, price, quantity)
        inventoryViewModel.updateInventory(inventory)
        findNavController().navigate(R.id.action_itemEditFragment_to_homeInventoryFragment)

    }

}