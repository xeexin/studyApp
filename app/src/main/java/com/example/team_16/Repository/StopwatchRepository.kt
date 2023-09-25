package com.example.team_16.Repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class StopwatchRepository {

    val email = FirebaseAuth.getInstance().currentUser?.email ?: "None"
    private val db = FirebaseFirestore.getInstance()
    val userRef = db.collection("Users").document(email)
    val timeRef = userRef.collection("studyTime")

    fun makeHash(
        Hours: Long?, Minutes: Long?, Seconds: Long?, MilliSeconds: Long?,
        TimeBuff: Long?, Major: String?): HashMap<String, *> {

        val data = hashMapOf(
            "hour" to Hours.toString(),
            "minute" to Minutes.toString(),
            "second" to Seconds.toString(),
            "millisecond" to MilliSeconds.toString(),
            "timeBuff" to TimeBuff.toString(),
            "major" to Major
        )
        return data
    }

    fun setData(date: LocalDate?, data: HashMap<String, *>){
        timeRef.document(date.toString())
            .set(data).addOnSuccessListener {
                Log.v("tag", "데이터가 들어갔습니다")
            }
            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents: $exception")
            }
    }

    fun getMajor(major: MutableLiveData<String>): LiveData<String>{
        userRef.get().addOnSuccessListener { document ->
            major.value = document.getString("department")
        }
        return major
    }

    //LiveData를 받아서 뷰모델에서 함수 사용하고 프래그먼트에서
    fun getHour(date: LocalDate?, h: MutableLiveData<Long>): LiveData<Long>{
        timeRef.document(date.toString())
            .get().addOnSuccessListener { document ->
                h.value = document.getString("hour")?.toLong() ?: 0

            }
        return h
    }

    fun getMinute(date: LocalDate?, m: MutableLiveData<Long>): LiveData<Long>{
        timeRef.document(date.toString())
            .get().addOnSuccessListener { document ->
                m.value = document.getString("minute")?.toLong() ?: 0
            }
        return m
    }

    fun getSecond(date: LocalDate?, s: MutableLiveData<Long>): LiveData<Long>{
        timeRef.document(date.toString())
            .get().addOnSuccessListener { document ->
                s.value = document.getString("second")?.toLong() ?: 0
            }
        return s
    }

    fun getMilli(date: LocalDate?, ms: MutableLiveData<Long>): LiveData<Long>{
        timeRef.document(date.toString())
            .get().addOnSuccessListener { document ->
                ms.value = document.getString("millisecond")?.toLong() ?: 0
            }
        return ms
    }

    fun getTimebuff(date: LocalDate?, tb: MutableLiveData<Long>): LiveData<Long>{
        timeRef.document(date.toString())
            .get().addOnSuccessListener { document ->
                tb.value = document.getString("timeBuff")?.toLong() ?: 0
            }
        return tb
    }
}