package com.example.zzzpicazzzas.data.di

import android.content.Context
import androidx.room.Room
import com.example.zzzpicazzzas.data.DATABASE_NAME
import com.example.zzzpicazzzas.data.local.db.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context) = Room.databaseBuilder(
        context = context,
        klass = Database::class.java,
        name = DATABASE_NAME,
    ).build()

    @Provides
    @Singleton
    fun providePizzaDao(database: Database) = database.pizzaDao()

}
