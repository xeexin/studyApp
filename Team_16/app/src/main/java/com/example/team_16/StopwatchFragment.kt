package com.example.team_16

import android.annotation.SuppressLint
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.team_16.databinding.FragmentStopwatchBinding
import com.example.team_16.repository.StopwatchRepository
//import com.example.team_16.repository.StopwatchRepository
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.timer

@RequiresApi(Build.VERSION_CODES.O)
@Suppress("DEPRECATION")
class Stopwatch : Fragment() {
    var email :String? = "None"
    var TimeBuff: Long = 0
    var isRunning: Boolean = false
    var major:String? = ""
    var Hours = 0
    var Seconds = 0
    var Minutes = 0
    var MilliSeconds = 0
    var timer: Timer?= null
    var handler: Handler? = null
    val today = SimpleDateFormat("yyyy-MM-dd")
        .format(Date(System.currentTimeMillis())) //오늘 날짜
    val tomorrow = SimpleDateFormat("yyyy-MM-dd")
        .format(Date(System.currentTimeMillis() - 1000*60*60*24))
    private lateinit var data: HashMap<String, *>
    private lateinit var binding: FragmentStopwatchBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        major = StopwatchRepository().getDepartment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStopwatchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStart.setOnClickListener {
            start()
        }
        binding.btnStop.setOnClickListener {
            stop()
            data = StopwatchRepository().makeHash(Hours, Minutes, Seconds, MilliSeconds, TimeBuff)
            StopwatchRepository().setData(today, data)
        }
    }

    override fun onStop() {
        super.onStop()
        data = StopwatchRepository().makeHash(Hours, Minutes, Seconds, MilliSeconds, TimeBuff)
        StopwatchRepository().setData(today, data)
    }

    private fun start(){
        if(isRunning) return
        isRunning = true
        timer = timer(period = 10){
            midnight()
            TimeBuff++
            Seconds = (TimeBuff / 100).toInt()
            Hours = Minutes / 60
            Minutes = Seconds / 60
            MilliSeconds = (TimeBuff % 100).toInt()
            handler = Handler(Looper.getMainLooper())
            handler?.post(Runnable {
                MainActivity().runOnUiThread {
                    binding.txtHour.setText(String.format("%02d", Hours))
                    binding.txtMin.setText(String.format("%02d", Minutes))
                    binding.txtSec.setText(String.format("%02d", Seconds))
                    binding.txtMil.setText(String.format("%02d", MilliSeconds))
                }
            })
        }
    }

    private fun stop(){
        isRunning = false
        timer?.cancel()
    }

    private fun midnight() {
        val date = Date()
        var calendar = Calendar.getInstance()
        calendar.setTime(date)
        var h = calendar.get(Calendar.HOUR_OF_DAY)
        var m = calendar.get(Calendar.MINUTE)
        var s = calendar.get(Calendar.SECOND)
        var ms = calendar.get(Calendar.MILLISECOND) / 10
        if (h == 0 && m == 0 && s == 0 && ms == 0) { // 시간 지정
            Log.v("알림", "조건문이 실행되었습니다")
            timer?.cancel()
            isRunning = false
            TimeBuff = 0
            Hours = 0
            Minutes = 0
            Seconds = 0
            MilliSeconds = 0

            data = StopwatchRepository().makeHash(Hours, Minutes, Seconds, MilliSeconds, TimeBuff)
           StopwatchRepository().setData(tomorrow, data)

            binding.root.post {
                binding.txtHour.setText("00")
                binding.txtMin.setText("00")
                binding.txtSec.setText("00")
                binding.txtMil.setText("00")
            }
        }
    }
}






