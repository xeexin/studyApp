package com.example.team_16.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.team_16.Repository.MyPageRepository

class MyPageViewModel: ViewModel() {
    val repository = MyPageRepository()

    private var _email = MutableLiveData<String>("email")
    private var _nickname = MutableLiveData<String>("nickname")
    private var _name = MutableLiveData<String>("name")
    private var _kauId = MutableLiveData<String>("kauid")
    private var _major = MutableLiveData<String>("department")

    val email: LiveData<String> = repository.getEmail(_email)
    val nickname: LiveData<String> = repository.getNickname(_nickname)
    val name: LiveData<String> = repository.getName(_name)
    val kauId: LiveData<String> = repository.getKauId(_kauId)
    val major: LiveData<String> = repository.getMajor(_major)

    fun changenick(nickname: String){
        _nickname.value = repository.changeNickname(nickname)
    }
}