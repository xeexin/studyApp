package com.example.team_16.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

class StatisticsRepository {
    private val email = FirebaseAuth.getInstance().currentUser?.email ?: "None"
    private val db = FirebaseFirestore.getInstance()
    val timeBuffRef = db.collection("Users").document(email).collection("studyTime")

    fun getTimeBuff(date: LocalDate?, timeBuff: MutableLiveData<Long>): LiveData<Long>{
        timeBuffRef.document(date.toString())
            .get().addOnSuccessListener { document ->
                timeBuff.value = document.getString("timeBuff")?.toLong()?.div(1000) ?: 0
            }
        return timeBuff
    }
}