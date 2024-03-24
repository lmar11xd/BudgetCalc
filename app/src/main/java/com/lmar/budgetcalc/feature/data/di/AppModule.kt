package com.lmar.budgetcalc.feature.data.di

import android.content.Context
import androidx.room.Room
import com.lmar.budgetcalc.feature.data.local.AppDatabase
import com.lmar.budgetcalc.feature.data.local.BudgetDao
import com.lmar.budgetcalc.feature.data.local.MaterialDao
import com.lmar.budgetcalc.feature.data.repo.BudgetRepoImpl
import com.lmar.budgetcalc.feature.data.repo.MaterialRepoImpl
import com.lmar.budgetcalc.feature.domain.repo.BudgetRepo
import com.lmar.budgetcalc.feature.domain.repo.MaterialRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesRoomBudgetDao(database: AppDatabase): BudgetDao {
        return database.budgetDao
    }

    @Provides
    fun providesRoomMaterialDao(database: AppDatabase): MaterialDao {
        return database.materialDao
    }

    @Singleton
    @Provides
    fun providesRoomDb(
        @ApplicationContext appContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            appContext.applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providesBudgetRepo(db: AppDatabase, @IoDispatcher dispatcher: CoroutineDispatcher): BudgetRepo {
        return BudgetRepoImpl(db.budgetDao, dispatcher)
    }

    @Provides
    @Singleton
    fun providesMaterialRepo(db: AppDatabase, @IoDispatcher dispatcher: CoroutineDispatcher): MaterialRepo {
        return MaterialRepoImpl(db.materialDao, dispatcher)
    }
}