package com.univalle.inventoryapp.view.fragments

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.univalle.inventoryapp.R
import com.univalle.inventoryapp.databinding.FragmentHomeInventoryBinding
import com.univalle.inventoryapp.utils.Prefs
import com.univalle.inventoryapp.view.adapters.InventoryAdapter
import com.univalle.inventoryapp.view.widget.WidgetProvider
import com.univalle.inventoryapp.viewmodel.InventoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeInventoryFragment : Fragment() {
    private lateinit var binding: FragmentHomeInventoryBinding
    private val inventoryViewModel: InventoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeInventoryBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controllers()
        viewModelObservers()
    }

    private fun controllers() {
        controllerOverSystemBackButton()
        listenerButtonExit()
        listenerButtonAdd()
    }

    private fun controllerOverSystemBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().moveTaskToBack(true)
        }
    }

    private fun listenerButtonExit() {
        binding.contentToolbar.iconExit.setOnClickListener {
            Prefs.setLoggedIn(requireContext(), false)
            forceWidgetUpdate()
            findNavController().navigate(R.id.action_homeInventoryFragment_to_authenticationFragment)
        }
    }

    private fun forceWidgetUpdate() {
        val context = requireContext()
        val appWidgetManager = AppWidgetManager.getInstance(context)

        val ids = appWidgetManager.getAppWidgetIds(
            ComponentName(context, WidgetProvider::class.java)
        )

        val updateIntent = Intent(context, WidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        }

        context.sendBroadcast(updateIntent)
    }

    private fun listenerButtonAdd() {
        binding.floatingActionButtonAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeInventoryFragment_to_addItemFragment)
        }
    }

    private fun viewModelObservers() {
        observeInventoryList()
        observeProgress()
    }

    private fun observeInventoryList() {
        inventoryViewModel.getListInventory()
        inventoryViewModel.listInventory.observe(viewLifecycleOwner) { listInventory ->
            val recycler = binding.recyclerview
            val layoutManager = LinearLayoutManager(context)
            recycler.layoutManager = layoutManager
            val adapter = InventoryAdapter(listInventory, findNavController())
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun observeProgress() {
        inventoryViewModel.progressState.observe(viewLifecycleOwner) { status ->
            if (status) {
                binding.progressBar.visibility = View.VISIBLE
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(1000L)
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

}