package com.example.tamtamduku.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.tamtamduku.data.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WorkerRepository {
    private inline fun <reified T> getCollectionAsList(collectionName: String): List<T> {
        return try {
            val inputStream = com.example.tamtamduku.TamtamApplication.appContext.assets.open("firestore_export.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val root = Gson().fromJson(jsonString, com.google.gson.JsonObject::class.java)
            val collectionJson = root.getAsJsonObject(collectionName)
            if (collectionJson != null) {
                val type = object : TypeToken<Map<String, T>>() {}.type
                val map: Map<String, T> = Gson().fromJson(collectionJson, type)
                map.values.toList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private inline fun <reified T> getCollectionAsMap(collectionName: String): Map<String, T> {
        return try {
            val inputStream = com.example.tamtamduku.TamtamApplication.appContext.assets.open("firestore_export.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val root = Gson().fromJson(jsonString, com.google.gson.JsonObject::class.java)
            val collectionJson = root.getAsJsonObject(collectionName)
            if (collectionJson != null) {
                val type = object : TypeToken<Map<String, T>>() {}.type
                Gson().fromJson(collectionJson, type)
            } else {
                emptyMap()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyMap()
        }
    }

    fun getUsers(): Flow<List<User>> = flow {
        val map = getCollectionAsMap<User>("users")
        emit(map.map { it.value.copy(id = it.key) })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWorkers(): Flow<List<VocaWorker>> = flow {
        val map = getCollectionAsMap<VocaWorker>("workers")
        emit(map.map { it.value.copy(id = it.key) })
    }

    fun getChatLists(): Flow<List<ChatList>> = flow {
        emit(getCollectionAsList("chatLists"))
    }

    fun getChatRooms(): Flow<Map<String, List<ChatMessage>>> = flow {
        emit(getCollectionAsMap("chatRooms"))
    }

    fun getTransactions(): Flow<List<TransactionData>> = flow {
        emit(getCollectionAsList("transactions"))
    }

    fun getNotifications(): Flow<List<Notification>> = flow {
        emit(getCollectionAsList("notifications"))
    }
}
