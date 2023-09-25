package com.example.team_16

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.team_16.Repository.TodoRepository
import com.example.team_16.databinding.FragmentTodoBinding
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)

class TodoFragment : Fragment() {
    private val repository = TodoRepository()
    private lateinit var binding : FragmentTodoBinding
    var dateStr = (LocalDate.now()).toString()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater)
        return binding?.root
    }
    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.todoRecycle.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.btnRe.setOnClickListener{
            repository.readTodo(dateStr, binding)
        }
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            repository.readTodo("${year}-${month + 1}-$dayOfMonth", binding)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            repository.readTodo(dateStr, binding)
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.addButton.setOnClickListener {
            val todo = binding.EditTodo.text.toString()
            binding.EditTodo.text.clear()
            repository.addTodo(dateStr, repository.makeHash(dateStr, todo))
            repository.readTodo(dateStr, binding)
    }
    }
}