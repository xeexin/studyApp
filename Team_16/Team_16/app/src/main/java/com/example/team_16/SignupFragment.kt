package com.example.team_16

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.team_16.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class SignupFragment : Fragment() {

    var binding : FragmentSignupBinding? = null
    lateinit var auth : FirebaseAuth
    lateinit var database : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding?.btnSignupRegister?.setOnClickListener{
            val email = binding?.etEnterEmail?.text.toString()
            val password = binding?.etEnterPw?.text.toString()
            val confirmPW = binding?.etEnterRepw?.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && confirmPW.isNotEmpty()){
                if(password == (confirmPW)){
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if(it.isSuccessful){
                            Toast.makeText(activity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_signupFragment_to_Register_user_infoFragment)

                        }else{
                            Toast.makeText(activity, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    Toast.makeText(activity, "패스워드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(activity, "모든 항목을 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}