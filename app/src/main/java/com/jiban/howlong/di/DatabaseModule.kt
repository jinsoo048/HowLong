package com.jiban.howlong.di

import android.content.Context
import com.jiban.howlong.data.AppDatabase
import com.jiban.howlong.data.character.CharacterDao
import com.jiban.howlong.data.gender.GenderDao
import com.jiban.howlong.data.number.NumberDao
import com.jiban.howlong.data.score.ScoreDao
import com.jiban.howlong.data.sum.SumDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideCharacterDao(appDatabase: AppDatabase): CharacterDao {
        return appDatabase.characterDao()
    }

    @Provides
    fun provideGenderDao(appDatabase: AppDatabase): GenderDao {
        return appDatabase.genderDao()
    }

    @Provides
    fun provideNumberDao(appDatabase: AppDatabase): NumberDao {
        return appDatabase.numberDao()
    }

    @Provides
    fun provideScoreDao(appDatabase: AppDatabase): ScoreDao {
        return appDatabase.scoreDao()
    }

    @Provides
    fun provideSumDao(appDatabase: AppDatabase): SumDao {
        return appDatabase.sumDao()
    }
}