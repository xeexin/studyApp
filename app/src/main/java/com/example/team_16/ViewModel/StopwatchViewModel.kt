package com.example.team_16.ViewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.team_16.Repository.StopwatchRepository
import java.time.LocalDate
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.timer

@RequiresApi(Build.VERSION_CODES.O)
class StopwatchViewModel: ViewModel() {
    val repository = StopwatchRepository()
    private var timer: Timer? = null

    lateinit var data: HashMap<String, *>

    private var isRunning = MutableLiveData<Boolean>(false)
    private var _hour = MutableLiveData<Long>(0)
    private var _min = MutableLiveData<Long>(0)
    private var _sec = MutableLiveData<Long>(0)
    private var _mil = MutableLiveData<Long>(0)
    private var _timebuff = MutableLiveData<Long>(0)

    private var _yesh = MutableLiveData<Long>(0)
    private var _yesm = MutableLiveData<Long>(0)
    private var _yess = MutableLiveData<Long>(0)
    private var _yesms = MutableLiveData<Long>(0)

    private var _date = MutableLiveData<LocalDate>(LocalDate.now())

    private var _major = MutableLiveData<String>("null")

    val hour: LiveData<Long> = repository.getHour(_date.value, _hour)
    val min: LiveData<Long> = repository.getMinute(_date.value, _min)
    val sec: LiveData<Long> = repository.getSecond(_date.value, _sec)
    val mil: LiveData<Long> = repository.getMilli(_date.value, _mil)
    val timebuff: LiveData<Long> = repository.getTimebuff(_date.value, _timebuff)

    val yesh: LiveData<Long> = repository.getHour(_date.value?.minusDays(1), _yesh)
    val yesm: LiveData<Long> = repository.getMinute(_date.value?.minusDays(1), _yesm)
    val yess: LiveData<Long> = repository.getSecond(_date.value?.minusDays(1), _yess)
    val yesms: LiveData<Long> = repository.getMilli(_date.value?.minusDays(1), _yesms)

    val date: LiveData<LocalDate> get() = _date

    val major: LiveData<String> = repository.getMajor(_major)

    fun timerStart() { // stopwatch 시작
        if (isRunning.value == true) return
        isRunning.postValue(true)
        timer = timer(period = 10) {
            midnight() // 자정이 되면 시간이 리셋되어야 함

            _timebuff.postValue(_timebuff.value?.inc())
            _mil.postValue(_timebuff.value?.rem(100))
            _hour.postValue(_timebuff.value?.div(60*60*100))
            _min.postValue(_timebuff.value?.rem(60*60*100)?.div(60*100))
            _sec.postValue(_timebuff.value?.rem(60*60*100)?.rem(60*100)?.div(100))
        }
    }

    fun timerStop() { // stopwatch 중지
        isRunning.value = false
        timer?.cancel()
    }


    private fun midnight(){ // 자정이 되면 리셋 후 재시작 하는 함수
        val resetTime = Calendar.getInstance()
        val h = resetTime.get(Calendar.HOUR_OF_DAY)
        val m = resetTime.get(Calendar.MINUTE)
        val s = resetTime.get(Calendar.SECOND)
        val ms = resetTime.get(Calendar.MILLISECOND) / 10
        _date.postValue(LocalDate.now())

        if (h == 0 && m == 0 && s == 0 && (ms == 0 || ms == 1)) { // 시간 지정
            Log.v("tag", "실행됨")
            timer?.cancel()
            isRunning.postValue(false) // timer stop


            data = repository.makeHash(_hour.value, _min.value, _sec.value, _mil.value, _timebuff.value, _major.value)
            Log.v("알림", "today:${_date.value}, yesterday:${_date.value?.minusDays(1)}")
            repository.setData(date.value, data)

            _yesh.postValue(_hour.value)
            _yesm.postValue(_min.value)
            _yess.postValue(_sec.value)
            _yesms.postValue(_mil.value)

            _timebuff.postValue(0)
            _hour.postValue(0)
            _min.postValue(0)
            _sec.postValue(0)
            _mil.postValue(0)
            _timebuff.postValue(0) // reset data

            data = repository.makeHash(_hour.value, _min.value, _sec.value, _mil.value, _timebuff.value, _major.value)
            repository.setData(date.value, data)

            timerStart()
        }
    }
}