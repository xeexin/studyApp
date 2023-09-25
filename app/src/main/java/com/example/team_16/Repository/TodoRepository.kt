package com.example.team_16.Repository

import android.annotation.SuppressLint
import com.example.team_16.TodoModel
import com.example.team_16.Todoadapter
import com.example.team_16.databinding.FragmentTodoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TodoRepository {
    private val db = FirebaseFirestore.getInstance()
    private val userid = FirebaseAuth.getInstance().currentUser?.email
    private var number:Long = 0
    private val userTodo = db.collection("Users").document("$userid").collection("Todo")
    fun makeHash(dateStr: String, description: String): HashMap<String, *> {
        val data = hashMapOf(
            "description" to description,
            "date" to dateStr,
            "counting" to number,
            "checking" to "0",
            "userid" to userid
        )
        return data
    }
    fun edit_check(dateStr:String, number_: Long, checking:String){
        userTodo.document("$dateStr $number_").update("checking", checking)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun readTodo(dateStr:String, binding: FragmentTodoBinding){
        userTodo.get()
            .addOnSuccessListener{result->
                val itemList = arrayListOf<TodoModel>()
                for (document in result){
                    val item = TodoModel(document["date"] as String, document["description"] as String, document["counting"] as Long, document["checking"] as String)
                    if(item.date == dateStr) {
                        itemList.add(item)
                        if(item.counting >= number)
                            number = item.counting+1
                    }
                }
                val adapter = Todoadapter(itemList)
                binding.todoRecycle.adapter = adapter
                adapter.notifyDataSetChanged()
            }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addTodo(dateStr:String, data: HashMap<String, *>){
        userTodo
            .document("$dateStr $number")
            .set(data)
            .addOnSuccessListener {}
    }
    fun deleteTodo(dateStr: String, number_:Long){
        userTodo
            .document("${dateStr} ${number_}")
            .delete()
    }
}