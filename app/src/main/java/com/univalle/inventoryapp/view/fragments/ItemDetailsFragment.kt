package com.univalle.inventoryapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.univalle.inventoryapp.R
import com.univalle.inventoryapp.databinding.FragmentItemDetailsBinding
import com.univalle.inventoryapp.model.Inventory
import com.univalle.inventoryapp.viewmodel.InventoryViewModel
import com.univalle.inventoryapp.utils.PriceFormatter

class ItemDetailsFragment : Fragment() {
    private lateinit var binding: FragmentItemDetailsBinding
    private val inventoryViewModel: InventoryViewModel by viewModels()
    private lateinit var receivedInventory: Inventory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInventory()
        controladores()
    }

    private fun controladores() {
        binding.buttonDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        binding.fabEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("dataInventory", receivedInventory)
            findNavController().navigate(R.id.action_itemDetailsFragment_to_itemEditFragment, bundle)
        }

        binding.toolbarDetail.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun dataInventory() {
        val receivedBundle = arguments
        receivedInventory = receivedBundle?.getSerializable("key") as Inventory
        binding.detailProductName.text = receivedInventory.name
        binding.detailProductPrice.text = PriceFormatter.formatPrice(receivedInventory.price)
        binding.detailProductQuantity.text = "${receivedInventory.quantity}"
        binding.detailProductTotal.text = PriceFormatter.formatPrice(inventoryViewModel.totalProducto(receivedInventory.price, receivedInventory.quantity))
    }

    private fun showDeleteConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.confirm_delete_title))
            .setMessage(getString(R.string.confirm_delete_message))
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                deleteInventory()
                Toast.makeText(requireContext(), getString(R.string.product_deleted), Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun deleteInventory(){
        inventoryViewModel.deleteInventory(receivedInventory)
        inventoryViewModel.getListInventory()
        findNavController().popBackStack()
    }
}