package com.example.travelapp.data

import com.example.travelapp.ui.theme.data.Trip
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
    private val tripCollection = db.collection("trips")

    // 1. Hàm lưu chuyến đi (suspend để chạy nền)
    suspend fun addTrip(trip: Trip): Boolean {
        return try {
            // Dùng trip.id làm Document ID luôn để dễ quản lý
            tripCollection.document(trip.id).set(trip).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // 2. Hàm lấy danh sách Real-time (Trả về Flow)
    // Tự động báo về UI mỗi khi database có thay đổi
    fun getTrips(): Flow<List<Trip>> = callbackFlow {
        val listener = tripCollection
            .orderBy("checkIn", Query.Direction.DESCENDING) // Sắp xếp ngày mới nhất lên đầu (tùy chọn)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error) // Nếu lỗi thì đóng luồng
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    // Chuyển đổi dữ liệu từ Firestore về List<Trip>
                    val trips = snapshot.toObjects(Trip::class.java)
                    trySend(trips) // Gửi list mới ra ngoài UI
                }
            }

        // Khi màn hình bị hủy, tự động ngắt kết nối để tiết kiệm pin
        awaitClose { listener.remove() }
    }
}
