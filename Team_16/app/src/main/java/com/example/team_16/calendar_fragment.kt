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
import com.example.team_16.repository.CalendarRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)

class Todo : Fragment() {
    private val repository = CalendarRepository()
    private lateinit var binding : FragmentCalendarFragmentBinding
    private lateinit var email_ :String
    var dateStr = (LocalDate.now()).toString()
    val itemList = arrayListOf<ListLayout>()
    var adapter = Todoadapter(itemList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        email_ = FirebaseAuth.getInstance().currentUser?.email ?: "None"
        binding = FragmentCalendarFragmentBinding.inflate(inflater)
        return binding?.root
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.todoRecycle.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.todoRecycle.adapter = adapter

        repository.readTodo(dateStr, adapter, itemList)

        binding.btnRe.setOnClickListener{
            repository.readTodo(dateStr, adapter, itemList)
        }
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            dateStr = "${year}-${month + 1}-$dayOfMonth"
            repository.readTodo(dateStr, adapter, itemList)

        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            repository.readTodo(dateStr, adapter, itemList)
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.addButton.setOnClickListener {
            val todo = binding.EditTodo.text.toString()
            binding.EditTodo.text.clear()
            val data = repository.makeHash(dateStr, todo)
            repository.addTodo(dateStr, adapter, data)
            repository.readTodo(dateStr, adapter, itemList)
        }

    }}