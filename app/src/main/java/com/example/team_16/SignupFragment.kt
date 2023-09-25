package com.example.team_16

import android.content.ContentValues
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.team_16.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class SignupFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    var binding: FragmentSignupBinding? = null

    lateinit var auth: FirebaseAuth
    private var database: DatabaseReference? = null


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
        register()
    }

    private fun register() {

        binding?.btnSignupRegister?.setOnClickListener {

            //회원가입
            val email = binding?.etEnterEmail?.text.toString()
            val password = binding?.etEnterPw?.text.toString()
            val confirmPW = binding?.etEnterRepw?.text.toString()
            //리얼타임디비
            val nickname = binding?.etNickname?.text.toString()
            val name = binding?.etName?.text.toString()
            val major = binding?.spDep?.selectedItem.toString()
            val kauid = binding?.etKauID?.text.toString()

            if (TextUtils.isEmpty(email)) {
                binding?.etEnterEmail?.setError("이메일을 입력하세요")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                binding?.etEnterPw?.setError("비밀번호를 입력하세요")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(confirmPW)) {
                binding?.etEnterRepw?.setError("비밀번호를 다시 입력하세요")
                return@setOnClickListener
            } else if (password != confirmPW) {
                binding?.etEnterRepw?.setError("비밀번호가 일치 하지 않습니다.")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(nickname)) {
                binding?.etNickname?.setError("닉네임을 입력하세요")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(name)) {
                binding?.etName?.setError("이름을 입력하세요")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(kauid)) {
                binding?.etKauID?.setError("학번을 입력하세요")
                return@setOnClickListener
            } else if (kauid.length != 10) {
                binding?.etKauID?.setError("학번은 10자리의 숫자 입니다.")
                return@setOnClickListener
            }

            //파이어스토어
            val user_info = hashMapOf(
                "email" to email,
                "nickname" to binding?.etNickname?.text.toString(),
                "name" to binding?.etName?.text.toString(),
                "major" to binding?.spDep?.selectedItem.toString(),
                "kauid" to binding?.etKauID?.text.toString(),
            )


            auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    //파이어스토어
                    db.collection("Users").document("$email")
                        .set(user_info)
                        .addOnSuccessListener { documentReference ->
                            Log.d(ContentValues.TAG, "DocumentSnapshot written with ID")
                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error adding document", e)
                        }
                    val bundle = Bundle().apply { putString("email", email) }
                    //리얼타임디비
                    database = FirebaseDatabase.getInstance().getReference("Users")
                    val User = UserModel(email, nickname, name, major, kauid)

                    database?.child(nickname)?.setValue(User)?.addOnSuccessListener {
                        val uid = auth.currentUser?.uid

                        val User = UserModel(email, nickname, name, major, kauid, uid)
                        database?.child(uid.toString())?.setValue(User)?.addOnSuccessListener {
                            binding?.etEnterEmail?.text?.clear()
                            binding?.etNickname?.text?.clear()
                            binding?.etName?.text?.clear()
                            binding?.etKauID?.text?.clear()
                        }
                        Toast.makeText(activity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(
                            R.id.action_signupFragment_to_entryFragment,
                            bundle
                        )
                    }
                }else {
                    Toast.makeText(activity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

