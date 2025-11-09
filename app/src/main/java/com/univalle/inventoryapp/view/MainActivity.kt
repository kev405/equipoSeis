package com.univalle.inventoryapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.univalle.inventoryapp.R

import com.univalle.inventoryapp.utils.WidgetUpdate

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ////////////////
        WidgetUpdate.saveValueForWidget(this,"780000")
    }
}