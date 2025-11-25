package com.univalle.inventoryapp.di

import android.content.Context
import androidx.room.Room
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.univalle.inventoryapp.data.InventoryDB
import com.univalle.inventoryapp.data.InventoryDao
import com.univalle.inventoryapp.utils.Constants.NAME_BD
import dagger.hilt.android.qualifiers.ApplicationContext
import com.google.firebase.auth.FirebaseAuth

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideInventoryDB(@ApplicationContext context: Context):InventoryDB {
        return Room.databaseBuilder(
            context.applicationContext,
            InventoryDB::class.java,
            NAME_BD
        ).build()
    }

    @Singleton
    @Provides
    fun provideInventoryDao(inventoryDB: InventoryDB): InventoryDao {
        return inventoryDB.inventoryDao()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}