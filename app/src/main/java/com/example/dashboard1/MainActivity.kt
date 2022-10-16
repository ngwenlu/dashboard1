package com.example.dashboard1

import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate


class MainActivity : AppCompatActivity() {
    //private lateinit var ourPieChart: PieChart
    //private lateinit var ourBarChart: BarChart
    private lateinit var ourLineChart1: LineChart
    private lateinit var ourLineChart2: LineChart
    private lateinit var ourLineChart3: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ourPieChart = findViewById(R.id.ourPieChart)
        //ourBarChart = findViewById(R.id.ourBarChart)
        ourLineChart1 = findViewById(R.id.ourLineChart1)
        ourLineChart2 = findViewById(R.id.ourLineChart2)
        ourLineChart3 = findViewById(R.id.ourLineChart3)

        saveCars()
        retrieveRecordsAndPopulateCharts()
    }

    //method for saving records in database
    fun saveCars() {

        val databaseHandlerCar: DatabaseHandlerCar = DatabaseHandlerCar(this)

        //constructor(carName: String?, carModel: String?, duration: Double,
        // speed: Double, dist: Double, plusGas: Double, usedGas: Double)
        val record1 = databaseHandlerCar.addCarDetails(CarModel("Sandra's Car", "Toyota RAV4", 20.0, 7.0, 87.0, 3.0, 2.9))
        val record2 = databaseHandlerCar.addCarDetails(CarModel("Work Car", "Honda CR-V", 130.0, 10.0, 90.0, 2.1, 1.1))
        val record3 = databaseHandlerCar.addCarDetails(CarModel("Family Car", "Toyota Tacoma", 23.55, 13.0, 89.0, 3.0, 2.1))
        val record4 = databaseHandlerCar.addCarDetails(CarModel("Main Car", "Honda Civic", 8.8, 30.0, 66.0, 3.1, 1.0))
    }

    fun retrieveRecordsAndPopulateCharts() {
        //creating the instance of DatabaseHandler class
        val databaseHandlerCar: DatabaseHandlerCar = DatabaseHandlerCar(this)
        //calling the retrieveCars method of DatabaseHandler class to read the records
        val car: List<CarModel> = databaseHandlerCar.retrieveCars()
        //create arrays for storing the values gotten
        val carNameArray = Array<String>(car.size) { "unnamed car" }
        val carModelArray= Array<String>(car.size) { "default model" }
        val durationArray = Array<Double>(car.size) { 0.0 }
        val speedArray = Array<Double>(car.size) { 0.0 }
        val distArray = Array<Double>(car.size) { 0.0 }

        val plusGasArray = Array<Double>(car.size) { 0.0 }
        val usedGasArray = Array<Double>(car.size) { 0.0 }
        val currGasArray = Array<Double>(car.size) { 0.0 }

        //add the records till done
        var index = 0
        for (c in car) {
            carNameArray[index] = c.carName
            carModelArray[index] = c.carMod
            durationArray[index] = c.duration
            speedArray[index] = c.speed
            distArray[index] = c.dist

            plusGasArray[index] = c.plusGas
            usedGasArray[index] = c.usedGas
            currGasArray[index] = c.currGas
            index++
        }
        //call the methods for populating the charts
        //populatePieChart(speedArray, carNameArray)
        //populateBarChart(distArray)
        ourLineChart1 =  populateLineChart(plusGasArray, ourLineChart1)
        ourLineChart2 =  populateLineChart(usedGasArray, ourLineChart2)
        ourLineChart3 =  populateLineChart(speedArray, ourLineChart3)

    }

    /*private fun populatePieChart(values: Array<Double>, labels: Array<String>) {
        //an array to store the pie slices entry
        val ourPieEntry = ArrayList<PieEntry>()
        var i = 0

        for (entry in values) {
            //converting to float
            var value = values[i].toFloat()
            var label = labels[i]
            //adding each value to the pieentry array
            ourPieEntry.add(PieEntry(value, label))
            i++
        }

        //assigning color to each slices
        val pieShades: ArrayList<Int> = ArrayList()
        pieShades.add(Color.parseColor("#0E2DEC"))
        pieShades.add(Color.parseColor("#B7520E"))
        pieShades.add(Color.parseColor("#5E6D4E"))
        pieShades.add(Color.parseColor("#DA1F12"))

        //add values to the pie dataset and passing them to the constructor
        val ourSet = PieDataSet(ourPieEntry, "")
        val data = PieData(ourSet)

        //setting the slices divider width
        ourSet.sliceSpace = 1f

        //populating the colors and data
        ourSet.colors = pieShades
        ourPieChart.data = data
        //setting color and size of text
        data.setValueTextColor(Color.WHITE)
        data.setValueTextSize(10f)

        //add an animation when rendering the pie chart
        ourPieChart.animateY(1400, Easing.EaseInOutQuad)
        //disabling center hole
        ourPieChart.isDrawHoleEnabled = false
        //do not show description text
        ourPieChart.description.isEnabled = false
        //legend enabled and its various appearance settings
        ourPieChart.legend.isEnabled = true
        ourPieChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
        ourPieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        ourPieChart.legend.isWordWrapEnabled = true

        //dont show the text values on slices e.g Antelope, impala etc
        ourPieChart.setDrawEntryLabels(false)
        //refreshing the chart
        ourPieChart.invalidate()

    }

    private fun populateBarChart(values: Array<Double>) {
        //adding values
        val ourBarEntries: ArrayList<BarEntry> = ArrayList()
        var i = 0

        for (entry in values) {
            var value = values[i].toFloat()
            ourBarEntries.add(BarEntry(i.toFloat(), value))
            i++
        }


        val barDataSet = BarDataSet(ourBarEntries, "")
        //set a template coloring
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        val data = BarData(barDataSet)
        ourBarChart.data = data
        //setting the x-axis
        val xAxis: XAxis = ourBarChart.xAxis
        //calling methods to hide x-axis gridlines
        ourBarChart.axisLeft.setDrawGridLines(false)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove legend
        ourBarChart.legend.isEnabled = false

        //remove description label
        ourBarChart.description.isEnabled = false

        //add animation
        ourBarChart.animateY(3000)
        //refresh the chart
        ourBarChart.invalidate()
    }*/

    private fun populateLineChart(values: Array<Double>, ourLineChart:LineChart): LineChart {
        val ourLineChartEntries: ArrayList<Entry> = ArrayList()

        var i = 0

        for (entry in values) {
            var value = values[i].toFloat()
            ourLineChartEntries.add(Entry(i.toFloat(), value))
            i++
        }
        val lineDataSet = LineDataSet(ourLineChartEntries, "")
        lineDataSet.setColors(*ColorTemplate.PASTEL_COLORS)
        val data = LineData(lineDataSet)
        ourLineChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = ourLineChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        ourLineChart.legend.isEnabled = false

        //remove description label
        ourLineChart.description.isEnabled = false

        //add animation
        ourLineChart.animateX(1000, Easing.EaseInSine)
        ourLineChart.data = data
        //refresh
        ourLineChart.invalidate()

        return ourLineChart
    }
}

/*class MainActivity : AppCompatActivity() {
    private lateinit var ourPieChart: PieChart
    private lateinit var ourBarChart: BarChart
    private lateinit var ourLineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ourPieChart = findViewById(R.id.ourPieChart)
        ourBarChart = findViewById(R.id.ourBarChart)
        ourLineChart = findViewById(R.id.ourLineChart)
        saveAnimals()
        retrieveRecordsAndPopulateCharts()
    }

    //method for saving records in database
    fun saveAnimals() {

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val record1 = databaseHandler.addAnimalDetails(AnimalModel(1, "Lion", 470, 7, 87))
        val record2 = databaseHandler.addAnimalDetails(AnimalModel(2, "Impala", 1879, 10, 90))
        val record3 = databaseHandler.addAnimalDetails(AnimalModel(3, "Leopard", 570, 13, 89))
        val record4 = databaseHandler.addAnimalDetails(AnimalModel(4, "Crocodile", 150, 30, 66))
    }

    fun retrieveRecordsAndPopulateCharts() {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the retreiveAnimals method of DatabaseHandler class to read the records
        val animal: List<AnimalModel> = databaseHandler.retreiveAnimals()
        //create arrays for storing the values gotten
        val animalIDArray = Array<Int>(animal.size) { 0 }
        val animalNameArray = Array<String>(animal.size) { "natgeo" }
        val animalNumberArray = Array<Int>(animal.size) { 0 }
        val animalAgeArray = Array<Int>(animal.size) { 0 }
        val animalGrowthArray = Array<Int>(animal.size) { 0 }

        //add the records till done
        var index = 0
        for (a in animal) {
            animalIDArray[index] = a.animalId
            animalNameArray[index] = a.animalName
            animalNumberArray[index] = a.totNumber
            animalAgeArray[index] = a.avgAge
            animalGrowthArray[index] = a.avgGrowth
            index++
        }
        //call the methods for populating the charts
        populatePieChart(animalNumberArray, animalNameArray)
        populateBarChart(animalAgeArray)
        populateLineChart(animalGrowthArray)

    }

    private fun populatePieChart(values: Array<Int>, labels: Array<String>) {
        //an array to store the pie slices entry
        val ourPieEntry = ArrayList<PieEntry>()
        var i = 0

        for (entry in values) {
            //converting to float
            var value = values[i].toFloat()
            var label = labels[i]
            //adding each value to the pieentry array
            ourPieEntry.add(PieEntry(value, label))
            i++
        }

        //assigning color to each slices
        val pieShades: ArrayList<Int> = ArrayList()
        pieShades.add(Color.parseColor("#0E2DEC"))
        pieShades.add(Color.parseColor("#B7520E"))
        pieShades.add(Color.parseColor("#5E6D4E"))
        pieShades.add(Color.parseColor("#DA1F12"))

        //add values to the pie dataset and passing them to the constructor
        val ourSet = PieDataSet(ourPieEntry, "")
        val data = PieData(ourSet)

        //setting the slices divider width
        ourSet.sliceSpace = 1f

        //populating the colors and data
        ourSet.colors = pieShades
        ourPieChart.data = data
        //setting color and size of text
        data.setValueTextColor(Color.WHITE)
        data.setValueTextSize(10f)

        //add an animation when rendering the pie chart
        ourPieChart.animateY(1400, Easing.EaseInOutQuad)
        //disabling center hole
        ourPieChart.isDrawHoleEnabled = false
        //do not show description text
        ourPieChart.description.isEnabled = false
        //legend enabled and its various appearance settings
        ourPieChart.legend.isEnabled = true
        ourPieChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
        ourPieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        ourPieChart.legend.isWordWrapEnabled = true

        //dont show the text values on slices e.g Antelope, impala etc
        ourPieChart.setDrawEntryLabels(false)
        //refreshing the chart
        ourPieChart.invalidate()

    }

    private fun populateBarChart(values: Array<Int>) {
        //adding values
        val ourBarEntries: ArrayList<BarEntry> = ArrayList()
        var i = 0

        for (entry in values) {
            var value = values[i].toFloat()
            ourBarEntries.add(BarEntry(i.toFloat(), value))
            i++
        }


        val barDataSet = BarDataSet(ourBarEntries, "")
        //set a template coloring
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        val data = BarData(barDataSet)
        ourBarChart.data = data
        //setting the x-axis
        val xAxis: XAxis = ourBarChart.xAxis
        //calling methods to hide x-axis gridlines
        ourBarChart.axisLeft.setDrawGridLines(false)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove legend
        ourBarChart.legend.isEnabled = false

        //remove description label
        ourBarChart.description.isEnabled = false

        //add animation
        ourBarChart.animateY(3000)
        //refresh the chart
        ourBarChart.invalidate()
    }

    private fun populateLineChart(values: Array<Int>) {
        val ourLineChartEntries: ArrayList<Entry> = ArrayList()

        var i = 0

        for (entry in values) {
            var value = values[i].toFloat()
            ourLineChartEntries.add(Entry(i.toFloat(), value))
            i++
        }
        val lineDataSet = LineDataSet(ourLineChartEntries, "")
        lineDataSet.setColors(*ColorTemplate.PASTEL_COLORS)
        val data = LineData(lineDataSet)
        ourLineChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = ourLineChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        ourLineChart.legend.isEnabled = false

        //remove description label
        ourLineChart.description.isEnabled = false

        //add animation
        ourLineChart.animateX(1000, Easing.EaseInSine)
        ourLineChart.data = data
        //refresh
        ourLineChart.invalidate()
    }
}*/