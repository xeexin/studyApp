package com.example.team_16

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.team_16.ViewModel.MyPageViewModel
import com.example.team_16.databinding.FragmentMypageAndLogoutBinding
import com.google.firebase.auth.FirebaseAuth


class MypageAndLogoutFragment : Fragment() {
    private var binding: FragmentMypageAndLogoutBinding? = null
    private val viewModel: MyPageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //  Inflate the layout for this fragment
        binding = FragmentMypageAndLogoutBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.major.observe(viewLifecycleOwner) {
            binding?.txtMyDept?.text = it
        }
        viewModel.email.observe(viewLifecycleOwner) {
            binding?.txtMyEmail?.text = it
        }
        viewModel.kauId.observe(viewLifecycleOwner) {
            binding?.txtMyKauId?.text = it
        }
        viewModel.name.observe(viewLifecycleOwner) {
            binding?.txtMyName?.text = it
        }
        viewModel.nickname.observe(viewLifecycleOwner) {
            binding?.EditNickname?.setText(it)
        }


        binding?.btnLogout?.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(activity, "로그아웃에 성공하였습니다.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_mypageAndLogoutFragment_to_entryFragment)
        }

        binding?.btnEditnick?.setOnClickListener{
            viewModel.changenick(binding?.EditNickname?.text.toString())
            Toast.makeText(activity, "닉네임을 변경하였습니다", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}