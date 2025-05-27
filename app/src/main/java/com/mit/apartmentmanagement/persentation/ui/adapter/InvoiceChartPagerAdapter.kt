package com.mit.apartmentmanagement.persentation.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.mit.apartmentmanagement.databinding.ItemInvoiceChartBinding
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly

class InvoiceChartPagerAdapter : RecyclerView.Adapter<InvoiceChartPagerAdapter.ChartViewHolder>() {

    private var apartmentInvoices: Map<String, List<InvoiceMonthly>> = emptyMap()
    private var apartmentNames: List<String> = emptyList()

    fun submitData(apartmentInvoices: Map<String, List<InvoiceMonthly>>, apartmentNames: List<String>) {
        this.apartmentInvoices = apartmentInvoices
        this.apartmentNames = apartmentNames
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        val binding = ItemInvoiceChartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
        if (position < apartmentNames.size) {
            val apartmentName = apartmentNames[position]
            val invoices = apartmentInvoices[apartmentName] ?: emptyList()
            holder.bind(apartmentName, invoices)
        }
    }

    override fun getItemCount(): Int = apartmentNames.size

    class ChartViewHolder(private val binding: ItemInvoiceChartBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        fun bind(apartmentName: String, invoices: List<InvoiceMonthly>) {
            binding.tvApartmentName.text = apartmentName
            
            if (invoices.isEmpty()) {
                binding.tvNoData.visibility = android.view.View.VISIBLE
                binding.invoiceChart.visibility = android.view.View.GONE
                return
            }
            binding.tvNoData.visibility = android.view.View.GONE
            binding.invoiceChart.visibility = android.view.View.VISIBLE

            setupChart()
            updateChart(invoices)
        }

        private fun setupChart() {
            binding.invoiceChart.apply {
                description.isEnabled = false
                legend.isEnabled = false
                setDrawGridBackground(false)
                setDrawBorders(false)
                
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f
                }
                
                axisLeft.apply {
                    setDrawGridLines(true)
                    setDrawAxisLine(false)
                    axisMinimum = 0f
                }
                
                axisRight.isEnabled = false
                
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(false)
                setPinchZoom(false)
            }
        }

        private fun updateChart(invoices: List<InvoiceMonthly>) {
            val entries = invoices.mapIndexed { index, invoice ->
                BarEntry(index.toFloat(), invoice.totalPrice.toFloat())
            }
            val months = invoices.map { it.invoiceTime }
            val colors = createGradientColors(entries.size)
            
            val dataSet = BarDataSet(entries, "Invoice Amount").apply {
                this.colors = colors
                valueTextColor = Color.parseColor("#284777") // mainColor
                valueTextSize = 10f
                setDrawValues(true)
                setValueFormatter(object : com.github.mikephil.charting.formatter.ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${String.format("%.0f", value)}"
                    }
                })
            }

            binding.invoiceChart.apply {
                data = BarData(dataSet)
                xAxis.valueFormatter = IndexAxisValueFormatter(months)
                animateY(1000)
                invalidate()
            }
        }

        private fun createGradientColors(size: Int): List<Int> {
            val mainColor = Color.parseColor("#284777") // mainColor
            val lightColor = Color.parseColor("#D6E3FF") // md_theme_primaryContainer
            val accentColor = Color.parseColor("#415F91") // md_theme_primary
            
            return when {
                size <= 1 -> listOf(mainColor)
                size <= 3 -> listOf(mainColor, accentColor, lightColor).take(size)
                else -> {
                    val colors = mutableListOf<Int>()
                    for (i in 0 until size) {
                        val ratio = i.toFloat() / (size - 1)
                        val color = when {
                            ratio <= 0.5f -> interpolateColor(mainColor, accentColor, ratio * 2)
                            else -> interpolateColor(accentColor, lightColor, (ratio - 0.5f) * 2)
                        }
                        colors.add(color)
                    }
                    colors
                }
            }
        }

        private fun interpolateColor(startColor: Int, endColor: Int, ratio: Float): Int {
            val startA = Color.alpha(startColor)
            val startR = Color.red(startColor)
            val startG = Color.green(startColor)
            val startB = Color.blue(startColor)

            val endA = Color.alpha(endColor)
            val endR = Color.red(endColor)
            val endG = Color.green(endColor)
            val endB = Color.blue(endColor)

            return Color.argb(
                (startA + ratio * (endA - startA)).toInt(),
                (startR + ratio * (endR - startR)).toInt(),
                (startG + ratio * (endG - startG)).toInt(),
                (startB + ratio * (endB - startB)).toInt()
            )
        }
    }
} 