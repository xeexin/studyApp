package com.example.team_16.Repository

import android.util.Log
import android.widget.Toast
import androidx.core.content.contentValuesOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.coroutineContext

class MyPageRepository {
    val email = FirebaseAuth.getInstance().currentUser?.email ?: "None"
    private val db = FirebaseFirestore.getInstance()
    private val userRef = db.collection("Users").document(email)

    fun getMajor(major: MutableLiveData<String>): LiveData<String> {
        Log.v("tag", "mypage email : $email")
        userRef.get().addOnSuccessListener { document ->
            major.value = document.getString("department")
        }
        return major
    }

    fun getEmail(email: MutableLiveData<String>): LiveData<String> {
        userRef.get().addOnSuccessListener { document ->
            email.value = document.getString("email")
        }
        return email
    }

    fun getKauId(id: MutableLiveData<String>): LiveData<String> {
        userRef.get().addOnSuccessListener { document ->
            id.value = document.getString("kauid")
        }
        return id
    }

    fun getNickname(nickname: MutableLiveData<String>): LiveData<String> {
        userRef.get().addOnSuccessListener { document ->
            nickname.value = document.getString("nickname")
        }
        return nickname
    }

    fun getName(name: MutableLiveData<String>): LiveData<String> {
        userRef.get().addOnSuccessListener { document ->
            name.value = document.getString("name")
        }
        return name
    }

    fun changeNickname(nickname_: String): String {
        userRef.update("nickname", nickname_)
        return nickname_
    }
}