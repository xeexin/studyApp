package com.example.team_16

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.team_16.databinding.TodoItemBinding
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("StaticFieldLeak")
val db = FirebaseFirestore.getInstance()
class Todoadapter(private var itemList_ : ArrayList<ListLayout>): RecyclerView.Adapter<Todoadapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList_.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: Todoadapter.ViewHolder, position: Int){
        holder.bind(itemList_[position])
    }

    class ViewHolder(private val binding: TodoItemBinding): RecyclerView.ViewHolder(binding.root){
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(itemList_: ListLayout){
            binding.todoDescription.text = itemList_.description
            if(itemList_.checking == "1") {
                binding.checkBox.isChecked = true
                binding.todoDescription.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            }
            else {
                binding.checkBox.isChecked = false
                binding.todoDescription!!.setPaintFlags(0)
            }
            binding.btnDelete.setOnClickListener{
                db.collection("Contacts").document("${itemList_.date} ${itemList_.counting}").delete()
                Toast.makeText(binding.root.context, "할 일이 삭제되었습니다! \n\"적용\"버튼을 눌러주세요!!", Toast.LENGTH_SHORT).show()
            }
            binding.checkBox.setOnClickListener{
                if(itemList_.checking == "0") {
                    val check = "1"
                    val data = hashMapOf(
                        "description" to itemList_.description,
                        "date" to itemList_.date,
                        "counting" to itemList_.counting,
                        "checking" to check,
                        "userid" to itemList_.userid
                    )
                    db.collection("Contacts").document("${itemList_.date} ${itemList_.counting}")
                        .set(data)
                    Toast.makeText(binding.root.context, "할 일을 완료하셨네요! \n\"적용\"버튼을 눌러주세요!!", Toast.LENGTH_SHORT).show()
                }
                else{
                    val check = "0"
                    val data = hashMapOf(
                        "description" to itemList_.description,
                        "date" to itemList_.date,
                        "counting" to itemList_.counting,
                        "checking" to check,
                        "userid" to itemList_.userid
                    )
                    db.collection("Contacts").document("${itemList_.date} ${itemList_.counting}").set(data)
                    Toast.makeText(binding.root.context, "이 일을 아직 완수하지 못하셨네요! \n\"적용\"버튼을 눌러주세요!!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}