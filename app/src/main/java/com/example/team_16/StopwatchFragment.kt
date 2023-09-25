package com.example.team_16

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context

import android.content.Intent
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.team_16.Repository.MyPageRepository
import com.example.team_16.Repository.StopwatchRepository
import com.example.team_16.databinding.FragmentStopwatchBinding
import com.example.team_16.ViewModel.StopwatchViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.HashMap


@RequiresApi(Build.VERSION_CODES.O)
@Suppress("DEPRECATION")
class StopwatchFragment : Fragment() {

    private val viewModel: StopwatchViewModel by activityViewModels()
    private val stopwatch = StopwatchRepository()
    private lateinit var data: HashMap<String, *>

    private var binding: FragmentStopwatchBinding? = null
    private var date = LocalDate.now()

    private lateinit var mContext: Context

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStopwatchBinding.inflate(inflater)

        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.hour.observe(viewLifecycleOwner){
            binding?.txtHour?.setText(String.format("%02d", it))
        }
        viewModel.min.observe(viewLifecycleOwner){
            binding?.txtMin?.setText(String.format("%02d", it))
        }
        viewModel.sec.observe(viewLifecycleOwner){
            binding?.txtSec?.setText(String.format("%02d", it))
        }
        viewModel.mil.observe(viewLifecycleOwner){
            binding?.txtMil?.setText(String.format("%02d", it))
        }

        viewModel.yesh.observe(viewLifecycleOwner){
            binding?.txtYesHour?.setText(String.format("%02d", it))
        }
        viewModel.yesm.observe(viewLifecycleOwner){
            binding?.txtYesMin?.setText(String.format("%02d", it))
        }
        viewModel.yess.observe(viewLifecycleOwner){
            binding?.txtYesSec?.setText(String.format("%02d", it))
        }
        viewModel.yesms.observe(viewLifecycleOwner){
            binding?.txtYesMil?.setText(String.format("%02d", it))
        }

        viewModel.date.observe(viewLifecycleOwner){
            date = viewModel.date.value
        }

        binding?.btnStart?.setOnClickListener {
            viewModel.timerStart()
            val intent = Intent(mContext, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(mContext, 0 , intent, PendingIntent.FLAG_IMMUTABLE)

            getSystemService(mContext, AlarmManager::class.java)?.setExact(
                AlarmManager.RTC_WAKEUP,
                SystemClock.elapsedRealtime() + 60*60*1000,
                pendingIntent
            )
         }

        binding?.btnStop?.setOnClickListener {
            viewModel.timerStop()

            data = stopwatch.makeHash(viewModel.hour.value?.toLong(), viewModel.min.value?.toLong(),
                viewModel.sec.value?.toLong(), viewModel.mil.value?.toLong(), viewModel.timebuff.value?.toLong(),
                viewModel.major.value)
            stopwatch.setData(date, data)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.timerStop()
        data = stopwatch.makeHash(viewModel.hour.value?.toLong(), viewModel.min.value?.toLong(),
            viewModel.sec.value?.toLong(), viewModel.mil.value?.toLong(), viewModel.timebuff.value?.toLong(),
            viewModel.major.value)
        stopwatch.setData(date, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}