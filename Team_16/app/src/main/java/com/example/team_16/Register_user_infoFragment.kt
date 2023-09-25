package com.example.team_16

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.team_16.databinding.FragmentRegisterUserInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register_user_infoFragment : Fragment() {

    var binding : FragmentRegisterUserInfoBinding? = null
    lateinit var auth : FirebaseAuth
    lateinit var database : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterUserInfoBinding.inflate(inflater)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val email = arguments?.getString("email")
// val department_data = arrayOf("경영학부","항공교통물류학부","항공운항학과","항공우주 및 기계공학부", "항공전자정보공학부", "소프트웨어학과", "스마트드론공학과", "AI자율주행시스템공학과", "자유전공학부")


        binding?.btnRegisterMypage?.setOnClickListener{
            val nickname = binding?.etNickname?.text.toString()
            val name = binding?.etName?.text.toString()
            val department = binding?.spDep?.selectedItem.toString()
            val kauid = binding?.etKauID?.text.toString()


            database = FirebaseDatabase.getInstance().getReference("Users")
            val User = User_Model(email, nickname, name, department, kauid)


            database.child(nickname).setValue(User).addOnSuccessListener {
                binding?.etNickname?.text?.clear()
                binding?.etName?.text?.clear()
                binding?.etKauID?.text?.clear()

                findNavController().navigate(R.id.action_Register_user_infoFragment_to_entryFragment)

                Toast.makeText(activity, "정보가 입력 되었습니다.",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(activity, "정보를 다시 입력하세요.",Toast.LENGTH_SHORT).show()
            }

            val user_info = hashMapOf(
                "email" to email,
                "nickname" to binding?.etNickname?.text.toString(),
                "name" to binding?.etName?.text.toString(),
                "department" to binding?.spDep?.selectedItem.toString(),
                "kauid" to binding?.etKauID?.text.toString(),
            )


            db.collection("Users").document("$email")
                .set(user_info)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot written with ID")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }
        }
    }
}