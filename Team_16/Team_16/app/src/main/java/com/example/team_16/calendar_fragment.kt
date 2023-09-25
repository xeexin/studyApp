package com.example.team_16

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.team_16.databinding.FragmentCalendarFragmentBinding
import com.example.team_16.databinding.FragmentMypageBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
var dateStr = (LocalDate.now()).toString()
var number = 0
var itemList = arrayListOf<ListLayout>()
var adapter = Todoadapter(itemList)
class calendar_fragment : Fragment() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var binding : FragmentCalendarFragmentBinding
    var email : String? = "None"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("email"){key, bundle->
            email = bundle.getString("email")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        email = arguments?.getString("email")
        // Inflate the layout for this fragment
        binding = FragmentCalendarFragmentBinding.inflate(inflater)
        return binding?.root
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        binding.todoRecycle.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.todoRecycle.adapter = adapter
        db.collection("Contacts")
            .get()
            .addOnSuccessListener{result->
                itemList.clear()
                for (document in result){
                    val item = ListLayout(document["date"] as String, document["description"] as String, document["counting"] as String, document["checking"] as String, document["userid"] as String)
                    if(item.date == dateStr&& email == item.userid) {
                        itemList.add(item)
                        if(item.counting.toInt() >= number)
                            number = item.counting.toInt()
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener{exception ->
                Log.w("MainActivity", "Error getting documents $exception")
            }
        binding.btnRe.setOnClickListener{
            db.collection("Contacts")
                .get()
                .addOnSuccessListener{result->
                    itemList.clear()
                    for (document in result){
                        val item = ListLayout(document["date"] as String, document["description"] as String, document["counting"] as String, document["checking"] as String, document["userid"] as String)
                        if(item.date == dateStr && email == item.userid) {
                            itemList.add(item)
                            if(item.counting.toInt() >= number)
                                number = item.counting.toInt()
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener{exception ->
                    Log.w("MainActivity", "Error getting documents $exception")
                }
        }
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            dateStr = "${year}-${month + 1}-$dayOfMonth"
            number = 0
            db.collection("Contacts")
                .get()
                .addOnSuccessListener{result->
                    itemList.clear()
                    for (document in result){
                        val item = ListLayout(document["date"] as String, document["description"] as String, document["counting"] as String, document["checking"] as String, document["userid"] as String)
                        if(item.date == dateStr && email == item.userid) {
                            itemList.add(item)
                            if(item.counting.toInt() >= number)
                                number = item.counting.toInt()
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener{exception ->
                    Log.w("MainActivity", "Error getting documents $exception")
                }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            db.collection("Contacts")
                .get()
                .addOnSuccessListener{result->
                    itemList.clear()
                    for (document in result){
                        val item = ListLayout(document["date"] as String, document["description"] as String, document["counting"] as String, document["checking"] as String, document["userid"] as String)
                        if(dateStr == item.date && email == item.userid)
                            itemList.add(item)
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener{exception ->
                    Log.w("MainActivity", "Error getting documents $exception")
                }
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.addButton.setOnClickListener {
            val todo = binding.EditTodo.text.toString()
            binding.EditTodo.text.clear()
            number = number + 1
            val data = hashMapOf(
                "description" to todo,
                "date" to dateStr,
                "counting" to number.toString(),
                "checking" to "0",
                "userid" to email
            )
            db.collection("Contacts")
                .document("${dateStr} $number")
                .set(data)
                .addOnSuccessListener {
                    Toast.makeText(binding.root.context, "할 일이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Log.w("MainActivity", "Error getting documents: $exception")
                }
            db.collection("Contacts")
                .get()
                .addOnSuccessListener{result->
                    itemList.clear()
                    for (document in result){
                        val item = ListLayout(document["date"] as String, document["description"] as String, document["counting"] as String, document["checking"] as String, document["userid"] as String)
                        if(dateStr == item.date && email == item.userid)
                            itemList.add(item)
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener{exception ->
                    Log.w("MainActivity", "Error getting documents $exception")
                }
        }
    }

}