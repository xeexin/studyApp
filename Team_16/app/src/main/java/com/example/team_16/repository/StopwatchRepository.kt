package com.example.team_16.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@RequiresApi(Build.VERSION_CODES.O)
class StopwatchRepository {
    val Stopwatch = Stopwatch()
    val email = FirebaseAuth.getInstance().currentUser?.email ?: "None"

    val db = FirebaseFirestore.getInstance()
    val userRef = db.collection("Users").document("$email")
    val timeRef = userRef.collection("studytime")

    fun getDepartment(): String?{
        var major: String? = null
        userRef.get().addOnSuccessListener{ document ->
            major = document.getString("department").toString()
        }
        return major
    }

    fun makeHash(Hours: Int, Minutes: Int, Seconds: Int, MilliSeconds: Int, TimeBuff: Long): HashMap<String, *> {
        val data = hashMapOf(
            "hour" to Hours.toString(),
            "minute" to Minutes.toString(),
            "second" to Seconds.toString(),
            "millisecond" to MilliSeconds.toString(),
            "timebuff" to TimeBuff.toString(),
            "major" to getDepartment()
        )
        return data
    }


    fun setData(date: String, data: HashMap<String, *>){
        timeRef.document("$date")
            .set(data).addOnSuccessListener {
                Log.v("알림", "데이터가 들어갔습니다")
            }
            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents: $exception")
            }
    }
}