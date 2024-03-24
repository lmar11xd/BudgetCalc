package com.lmar.budgetcalc.feature.data.di

import com.lmar.budgetcalc.feature.data.local.AppDatabase
import com.lmar.budgetcalc.feature.data.repo.BudgetRepoImpl
import com.lmar.budgetcalc.feature.data.repo.MaterialRepoImpl
import com.lmar.budgetcalc.feature.domain.repo.BudgetRepo
import com.lmar.budgetcalc.feature.domain.repo.MaterialRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class  IoDispatcher

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}