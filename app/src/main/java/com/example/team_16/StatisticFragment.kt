package com.example.team_16

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarEntry
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.team_16.ViewModel.StatisticsViewModel
import com.example.team_16.databinding.FragmentStatisticBinding
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class StatisticFragment: Fragment() {

    private var _date = MutableLiveData(LocalDate.now())
    private lateinit var binding: FragmentStatisticBinding
    private val viewModel = StatisticsViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticBinding.inflate(inflater)
        val barChart: BarChart = binding.chart
        initBarChart(barChart)
        setData(barChart)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val barChart: BarChart = binding.chart

        binding.btnGraph.setOnClickListener{
            initBarChart(barChart)
            setData(barChart)
        }
    }
    // 바 차트 설정
    private fun initBarChart(barChart: BarChart) {
        // 차트 회색 배경 설정 (default = false)
        barChart.setDrawGridBackground(false)
        // 막대 그림자 설정 (default = false)
        barChart.setDrawBarShadow(false)
        // 차트 테두리 설정 (default = false)
        barChart.setDrawBorders(false)
        barChart.setTouchEnabled(false)
        barChart.setMaxVisibleValueCount(7)
        val description = Description()
        // 오른쪽 하단 모서리 설명 레이블 텍스트 표시 (default = false)
        description.isEnabled = false
        barChart.description = description

        // X, Y 바의 애니메이션 효과
        barChart.animateY(1000)
        barChart.animateX(1000)

        // 바텀 좌표 값
        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 8f
        xAxis.granularity = 1f
        xAxis.textColor = Color.parseColor("#002870")
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = MyXAxisFormatter()


        val leftAxis: YAxis = barChart.axisLeft
        // 좌측 선 설정 (default = true)
        leftAxis.setDrawAxisLine(false)
        // 좌측 텍스트 컬러 설정
        leftAxis.textColor = Color.parseColor("#002870")
        leftAxis.granularity = 1f
        leftAxis.axisMinimum = 0f
        if(viewModel.hour1.value?.minus(3*60*60)!! <= 0 && viewModel.hour2.value?.minus(3*60*60)!! <= 0 &&
            viewModel.hour3.value?.minus(3*60*60)!! <= 0 && viewModel.hour4.value?.minus(3*60*60)!! <= 0 &&
            viewModel.hour5.value?.minus(3*60*60)!! <= 0 && viewModel.hour6.value?.minus(3*60*60)!! <= 0 &&
            viewModel.hour7.value?.minus(3*60*60)!! <= 0){ // 공부시간이 3시간 이하라면
            leftAxis.axisMaximum = 6f
        }
        else if(viewModel.hour1.value?.minus(6*60*60)!! <= 0 && viewModel.hour2.value?.minus(6*60*60)!! <= 0 &&
            viewModel.hour3.value?.minus(6*60*60)!! <= 0 && viewModel.hour4.value?.minus(6*60*60)!! <= 0 &&
            viewModel.hour5.value?.minus(6*60*60)!! <= 0 && viewModel.hour6.value?.minus(6*60*60)!! <= 0 &&
            viewModel.hour7.value?.minus(6*60*60)!! <= 0){ // 공부시간이 3시간 초과 6시간 이하라면
            leftAxis.axisMaximum = 6f
        }
        else if(viewModel.hour1.value?.minus(12*60*60)!! <= 0 && viewModel.hour2.value?.minus(12*60*60)!! <= 0 &&
            viewModel.hour3.value?.minus(12*60*60)!! <= 0 && viewModel.hour4.value?.minus(12*60*60)!! <= 0 &&
            viewModel.hour5.value?.minus(12*60*60)!! <= 0 && viewModel.hour6.value?.minus(12*60*60)!! <= 0 &&
            viewModel.hour7.value?.minus(12*60*60)!! <= 0){ // 공부시간이 6시간 초과 12시간 이하라면
            leftAxis.axisMaximum = 12f
        }
        else if(viewModel.hour1.value?.minus(18*60*60)!! <= 0 && viewModel.hour2.value?.minus(18*60*60)!! <= 0 &&
            viewModel.hour3.value?.minus(18*60*60)!! <= 0 && viewModel.hour4.value?.minus(18*60*60)!! <= 0 &&
            viewModel.hour5.value?.minus(18*60*60)!! <= 0 && viewModel.hour6.value?.minus(18*60*60)!! <= 0 &&
            viewModel.hour7.value?.minus(18*60*60)!! <= 0){ // 공부시간이 12시간 초과 18시간 이하라면
            leftAxis.axisMaximum = 18f
        }
        else{ // 공부시간이 18시간 초과 24시간 이하라면
            leftAxis.axisMaximum = 24f
        }

        val rightAxis: YAxis = barChart.axisRight
        rightAxis.isEnabled = false
        // 우측 선 설정 (default = true)
        rightAxis.setDrawAxisLine(false)

        // 바차트의 타이틀
        val legend: Legend = barChart.legend
        //legend.form = Legend.LegendForm.LINE
        legend.textSize = 10f
        legend.textColor = Color.parseColor("#002870")
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
    }

    // 차트 데이터 설정
    private fun setData(barChart: BarChart) {
        // Zoom In / Out 가능 여부 설정
        barChart.setScaleEnabled(false)

        val valueList = ArrayList<BarEntry>()
        val title = "단위 : 시간"
        // 데이터

        valueList.add(BarEntry(1f, viewModel.hour1.value?.toFloat()?.div(60*60) ?: 0f))
        valueList.add(BarEntry(2f, viewModel.hour2.value?.toFloat()?.div(60*60) ?: 0f))
        valueList.add(BarEntry(3f, viewModel.hour3.value?.toFloat()?.div(60*60) ?: 0f))
        valueList.add(BarEntry(4f, viewModel.hour4.value?.toFloat()?.div(60*60) ?: 0f))
        valueList.add(BarEntry(5f, viewModel.hour5.value?.toFloat()?.div(60*60) ?: 0f))
        valueList.add(BarEntry(6f, viewModel.hour6.value?.toFloat()?.div(60*60) ?: 0f))
        valueList.add(BarEntry(7f, viewModel.hour7.value?.toFloat()?.div(60*60) ?: 0f))


        val barDataSet = BarDataSet(valueList, title)
        barDataSet.setColor(Color.BLUE)

        val data = BarData(barDataSet)
        barDataSet.color = Color.parseColor("#002870")
        barChart.data = data
        barChart.invalidate()
    }



    inner class MyXAxisFormatter : ValueFormatter() {
        private val days = arrayOf("${_date.value?.minusDays(6)}",
            "${_date.value?.minusDays(5)}",
            "${_date.value?.minusDays(4)}",
            "${_date.value?.minusDays(3)}",
            "${_date.value?.minusDays(2)}",
            "${_date.value?.minusDays(1)}",
            "${_date.value}")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()-1) ?: value.toString()
        }
    }
}