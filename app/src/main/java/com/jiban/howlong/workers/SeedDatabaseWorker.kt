package com.jiban.howlong.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.jiban.howlong.data.AppDatabase
import com.jiban.howlong.data.both.Both
import com.jiban.howlong.data.character.Character
import com.jiban.howlong.data.female.Female
import com.jiban.howlong.data.gender.Gender
import com.jiban.howlong.data.male.Male
import com.jiban.howlong.data.number.Number
import com.jiban.howlong.data.score.Score
import com.jiban.howlong.data.sum.Sum
import com.jiban.howlong.utilities.*
import kotlinx.coroutines.coroutineScope


class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {

        val database = AppDatabase.getInstance(applicationContext)

        try {
            applicationContext.assets.open(CHARACTER_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val characterType = object : TypeToken<List<Character>>() {}.type
                    val characterList: List<Character> =
                        Gson().fromJson(jsonReader, characterType)

                    database.characterDao().insertAll(characterList)
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }

        try {
            applicationContext.assets.open(GENDER_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val genderType = object : TypeToken<List<Gender>>() {}.type
                    val genderList: List<Gender> =
                        Gson().fromJson(jsonReader, genderType)

                    database.genderDao().insertAll(genderList)
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }

        try {
            applicationContext.assets.open(NUMBER_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val numberType = object : TypeToken<List<Number>>() {}.type
                    val numberList: List<Number> =
                        Gson().fromJson(jsonReader, numberType)

                    database.numberDao().insertAll(numberList)
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }

        try {
            applicationContext.assets.open(SCORE_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val scoreType = object : TypeToken<List<Score>>() {}.type
                    val scoreList: List<Score> =
                        Gson().fromJson(jsonReader, scoreType)

                    database.scoreDao().insertAll(scoreList)
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }

        try {
            applicationContext.assets.open(SUM_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val sumType = object : TypeToken<List<Sum>>() {}.type
                    val sumList: List<Sum> =
                        Gson().fromJson(jsonReader, sumType)

                    database.sumDao().insertAll(sumList)
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }

        try {
            applicationContext.assets.open(MALE_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val maleType = object : TypeToken<List<Male>>() {}.type
                    val maleList: List<Male> =
                        Gson().fromJson(jsonReader, maleType)

                    database.maleDao().insertAll(maleList)
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }

        try {
            applicationContext.assets.open(FEMALE_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val femaleType = object : TypeToken<List<Female>>() {}.type
                    val femaleList: List<Female> =
                        Gson().fromJson(jsonReader, femaleType)

                    database.femaleDao().insertAll(femaleList)
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }

        try {
            applicationContext.assets.open(BOTH_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val bothType = object : TypeToken<List<Both>>() {}.type
                    val bothList: List<Both> =
                        Gson().fromJson(jsonReader, bothType)

                    database.bothDao().insertAll(bothList)
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "DeedDatabaseWorker"
    }
}
