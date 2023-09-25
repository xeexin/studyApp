package com.example.team_16.repository

import android.annotation.SuppressLint
import com.example.team_16.ListLayout
import com.example.team_16.Todoadapter
import com.example.team_16.db
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CalendarRepository {
    private val db_ = FirebaseFirestore.getInstance()
    private val userid = FirebaseAuth.getInstance().currentUser?.email
    private var number = 0

    fun makeHash(dateStr: String, description: String, checking:String="None"): HashMap<String, *> {
        val data = hashMapOf(
            "description" to description,
            "date" to dateStr,
            "counting" to number.toString(),
            "checking" to checking,
            "userid" to userid
        )
        return data
    }

    @SuppressLint("NotifyDataSetChanged")
    fun readTodo(dateStr:String, adapter: Todoadapter, ItemList:ArrayList<ListLayout>):ArrayList<ListLayout>{
        db.collection("Users")
            .document("$userid")
            .collection("Todo")
            .get()
            .addOnSuccessListener{result->
                for (document in result){
                    ItemList.clear()
                    val item = ListLayout(document["date"] as String, document["description"] as String, document["counting"] as String, document["checking"] as String)
                    if(item.date == dateStr) {
                        ItemList.add(item)
                        if(item.counting.toInt() >= number)
                            number = item.counting.toInt()+1
                    }
                }
                adapter.notifyDataSetChanged()
            }
        return ItemList
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addTodo(dateStr:String, adapter: Todoadapter, data: HashMap<String, *>, number_:Int = number){
        db.collection("Users")
            .document("$userid")
            .collection("Todo")
            .document("$dateStr $number_")
            .set(data)
            .addOnSuccessListener { adapter.notifyDataSetChanged() }
    }

    fun deleteTodo(dateStr: String, number_:String = number.toString()){
        db.collection("Users")
            .document("$userid")
            .collection("Todo")
            .document("${dateStr} ${number_}")
            .delete()
    }

}