package com.example.team_16

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.team_16.databinding.FragmentEntryBinding
import com.example.team_16.databinding.FragmentNewpwBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


class FindePWFragment : Fragment() {

    var binding : FragmentNewpwBinding? = null
    lateinit var auth : FirebaseAuth
    lateinit var database : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewpwBinding.inflate(inflater)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding?.btnNewPw?.setOnClickListener{
            val email = binding?.etEmailtosend?.text.toString()
            val newpassword = binding?.etNewPW?.text.toString()
            val newconfirmPW = binding?.etReNewPW?.text.toString()

            if(email.isNotEmpty() && newpassword.isNotEmpty() && newconfirmPW.isNotEmpty()) {
                if (newpassword == (newconfirmPW)) {
                    auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity, "재설정 메일을 보냈습니다.", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_findePWFragment_to_entryFragment3)

                            } else {
                                Toast.makeText(activity, task.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }

}

