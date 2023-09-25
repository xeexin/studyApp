package com.example.team_16

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.team_16.databinding.FragmentStopwatchBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@Suppress("DEPRECATION")
class StopwatchFragment : Fragment() {

    var MillisecondTime: Long = 0
    var StartTime: Long = 0
    var TimeBuff: Long = 0
    var UpdateTime = 0L
    var handler = Handler()
    var isRunning: Boolean = false

    var Hours = 0
    var Seconds = 0
    var Minutes = 0
    var MilliSeconds = 0

    val db = FirebaseFirestore.getInstance()

    @SuppressLint("SimpleDateFormat")
    var today = SimpleDateFormat("yyyy-MM-dd")
        .format(Date(System.currentTimeMillis())) //오늘 날짜

    private lateinit var binding: FragmentStopwatchBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = arguments?.getString("email")
        setFragmentResult("email", bundleOf("email" to email))
        val docRef = db.collection("studytime").document("$today")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val H = document.getString("hour")
                    val M = document.getString("minute")
                    val S = document.getString("second")
                    val MS = document.getString("millisecond")
                    val TB = document.getString("timebuff")
                    if (H != null && M != null && S != null && MS != null && TB != null) {
                        Hours = H.toInt()
                        Minutes = M.toInt()
                        Seconds = S.toInt()
                        MilliSeconds = MS.toInt()
                        TimeBuff = TB.toLong()
                        binding.root.post {
                            binding.txtTime.setText(
                                "" + String.format("%02d", Hours) + ":" +
                                        String.format("%02d", Minutes) + ":" +
                                        String.format("%02d", Seconds) + "." +
                                        String.format("%03d", MilliSeconds)
                            )
                            Log.v("알림", "셋팅되었습니다")
                        }
                    }
                }
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStopwatchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnStart.setOnClickListener {
            if (!isRunning) { // 중복호출 방지
                StartTime = SystemClock.elapsedRealtime()
                isRunning = true
                handler?.postDelayed(runnable, 0)

            }
        }
        binding.btnStop.setOnClickListener {
            TimeBuff = UpdateTime
            handler?.removeCallbacks(runnable)
            isRunning = false

            val data = hashMapOf(
                "hour" to Hours.toString(),
                "minute" to Minutes.toString(),
                "second" to Seconds.toString(),
                "millisecond" to MilliSeconds.toString(),
                "timebuff" to TimeBuff.toString()
            )

            db.collection("studytime")
                .document("$today")
                .set(data)
                .addOnSuccessListener {
                    Log.v("알림", "button 데이터가 들어갔습니다")
                }
                .addOnFailureListener { exception ->
                    Log.w("MainActivity", "Error getting documents: $exception")
                }

        }
    }

    override fun onStop() {
        super.onStop()
        TimeBuff = UpdateTime
        handler?.removeCallbacks(runnable)
        isRunning = false

        val data = hashMapOf(
            "hour" to Hours.toString(),
            "minute" to Minutes.toString(),
            "second" to Seconds.toString(),
            "millisecond" to MilliSeconds.toString(),
            "timebuff" to TimeBuff.toString()
        )

        db.collection("studytime")
            .document("$today")
            .set(data)
            .addOnSuccessListener {
                Log.v("알림", "stop데이터가 들어갔습니다")
            }
            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents: $exception")
            }
    }

    var runnable: Runnable = object : Runnable {
        override fun run() {
            MillisecondTime = SystemClock.elapsedRealtime() - StartTime //흘러간 시간
            UpdateTime = TimeBuff + MillisecondTime

            Seconds = (UpdateTime / 1000).toInt()
            Hours = Minutes / 60
            Minutes = Seconds / 60
            Seconds = Seconds % 60
            MilliSeconds = (UpdateTime % 1000).toInt()

            binding.txtTime.setText(
                "" + String.format("%02d", Hours) + ":" +
                        String.format("%02d", Minutes) + ":" +
                        String.format("%02d", Seconds) + "." +
                        String.format("%03d", MilliSeconds)
            )
            handler.postDelayed(this, 0)
            midnight()
        }
    }

    fun midnight() {
        val date = Date()
        var calendar = Calendar.getInstance()
        calendar.setTime(date)
        var h = calendar.get(Calendar.HOUR_OF_DAY)
        var m = calendar.get(Calendar.MINUTE)
        var s = calendar.get(Calendar.SECOND)
        var ms = calendar.get(Calendar.MILLISECOND) / 10
        if (h == 11 && m == 48 && s == 0 && ms == 0) { // 시간 지정
            Log.v("알림", "조건문이 실행되었습니다")
            handler.removeCallbacks(runnable)
            isRunning = false
            TimeBuff = 0
            Hours = 0
            Minutes = 0
            Seconds = 0
            MilliSeconds = 0
            UpdateTime = 0L
            Log.v("알림", "Setting $Hours $Minutes $Seconds $MilliSeconds $TimeBuff")
            val data = hashMapOf(
                "hour" to Hours.toString(),
                "minute" to Minutes.toString(),
                "second" to Seconds.toString(),
                "millisecond" to MilliSeconds.toString(),
                "timebuff" to TimeBuff.toString()
            )
            Log.v("알림", "set data $Hours $Minutes $Seconds $MilliSeconds $TimeBuff")
            db.collection("studytime")
                .document("$today")
                .set(data)
                .addOnSuccessListener {
                    Log.v("알림", "put data $Hours $Minutes $Seconds $MilliSeconds $TimeBuff")
                }
                .addOnFailureListener { exception ->
                    Log.w("MainActivity", "Error getting documents: $exception")
                }
            binding.root.post {
                binding.txtTime.setText("00:00:00.000")
                Log.v("알림", "조건문이 실행되었습니다2")
            }


        }


    }

}






