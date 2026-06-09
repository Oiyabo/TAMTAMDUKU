package com.example.tamtamduku.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.tamtamduku.data.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class WorkerRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val gson = Gson()

    private inline fun <reified T> mapDocument(doc: com.google.firebase.firestore.DocumentSnapshot): T? {
        return try {
            val json = gson.toJson(doc.data)
            gson.fromJson(json, T::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getUsers(): Flow<List<User>> = callbackFlow {
        val listener = firestore.collection("users")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val users = snapshot?.documents?.mapNotNull { doc ->
                    mapDocument<User>(doc)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(users)
            }
        awaitClose { listener.remove() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWorkers(): Flow<List<VocaWorker>> = callbackFlow {
        val listener = firestore.collection("workers")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val workers = snapshot?.documents?.mapNotNull { doc ->
                    mapDocument<VocaWorker>(doc)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(workers)
            }
        awaitClose { listener.remove() }
    }

    fun getChatLists(): Flow<List<ChatList>> = callbackFlow {
        val listener = firestore.collection("chatLists")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val items = snapshot?.documents?.mapNotNull { doc ->
                    mapDocument<ChatList>(doc)
                } ?: emptyList()
                trySend(items)
            }
        awaitClose { listener.remove() }
    }

    fun getChatRooms(): Flow<Map<String, List<ChatMessage>>> = callbackFlow {
        val listener = firestore.collection("chatRooms")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val map = mutableMapOf<String, List<ChatMessage>>()
                snapshot?.documents?.forEach { doc ->
                    val messagesList = doc.get("messages") as? List<Map<String, Any>>
                    if (messagesList != null) {
                        val messages = messagesList.mapNotNull { msgMap ->
                            try {
                                val json = gson.toJson(msgMap)
                                gson.fromJson(json, ChatMessage::class.java)
                            } catch (e: Exception) { null }
                        }
                        map[doc.id] = messages
                    }
                }
                trySend(map)
            }
        awaitClose { listener.remove() }
    }

    fun getTransactions(): Flow<List<TransactionData>> = callbackFlow {
        val listener = firestore.collection("transactions")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val items = snapshot?.documents?.mapNotNull { doc ->
                    mapDocument<TransactionData>(doc)
                } ?: emptyList()
                trySend(items)
            }
        awaitClose { listener.remove() }
    }

    fun getNotifications(): Flow<List<Notification>> = callbackFlow {
        val listener = firestore.collection("notifications")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val items = snapshot?.documents?.mapNotNull { doc ->
                    mapDocument<Notification>(doc)
                } ?: emptyList()
                trySend(items)
            }
        awaitClose { listener.remove() }
    }
}
