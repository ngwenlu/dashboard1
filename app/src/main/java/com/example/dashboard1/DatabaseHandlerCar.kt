package com.example.dashboard1

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException

class DatabaseHandlerCar(context: Context): SQLiteOpenHelper(context,DB_NAME,null,DB_VERSION) {
    companion object {
        private val DB_VERSION = 1
        private val DB_NAME = "wildDB"
        private val MAIN_TABLE = "mainTable"

        private val NAME_FIELD = "name"
        private val MODEL_FIELD = "model"
        private val DURATION_FIELD = "duration"
        private val SPEED_FIELD = "ave_speed"
        private val DIST_FIELD = "distance"
        private val PLUSGAS_FIELD = "plus_gas"
        private val USEDGAS_FIELD = "used_gas"
        private val CURRGAS_FIELD = "curr_gas"
    }
    override fun onCreate(ourDB: SQLiteDatabase?) {
        //creating our table with the respective fields
        val CREATE_MAIN_TABLE = ("CREATE TABLE " + MAIN_TABLE + "("
                + NAME_FIELD + " TEXT,"
                + MODEL_FIELD + " TEXT,"
                + DURATION_FIELD + " DOUBLE,"
                + SPEED_FIELD + " DOUBLE,"
                + DIST_FIELD + " DOUBLE,"
                + PLUSGAS_FIELD + " DOUBLE, "
                + USEDGAS_FIELD + " DOUBLE,"
                + CURRGAS_FIELD + " DOUBLE" +
                ")")
        //executing the create table query
        ourDB?.execSQL(CREATE_MAIN_TABLE)
    }

    //function to be invoked when upgrading your database
    override fun onUpgrade(ourDB: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        ourDB!!.execSQL("DROP TABLE IF EXISTS " + MAIN_TABLE)
        onCreate(ourDB)
    }


    //a method to insert records
    fun addCarDetails(car: CarModel):Long{
        //opening the database in a writable mode to be able to make changes in it
        val ourDB = this.writableDatabase
        val ourContentValues = ContentValues()
        ourContentValues.put(NAME_FIELD, car.carName)
        ourContentValues.put(MODEL_FIELD, car.carMod)
        ourContentValues.put(DURATION_FIELD, car.duration)
        ourContentValues.put(SPEED_FIELD, car.speed)
        ourContentValues.put(DIST_FIELD, car.dist)
        ourContentValues.put(PLUSGAS_FIELD, car.plusGas)
        ourContentValues.put( USEDGAS_FIELD, car.usedGas)
        ourContentValues.put(CURRGAS_FIELD, car.currGas)
        val success = ourDB.insert(MAIN_TABLE, null, ourContentValues)
        //close the database
        ourDB.close()
        return success
    }

    //method to read the animal records
    @SuppressLint("Range")
    fun retrieveCars():List<CarModel>{
        //a list to be returned after fetching the records
        val carList:ArrayList<CarModel> = ArrayList<CarModel>()
        //the SELECT query
        val selectQuery = "SELECT  * FROM $MAIN_TABLE"
        //we open the database in a readable mode for fetching the records
        val ourDB = this.readableDatabase
        //cursor for storing the retrieved records
        var ourCursor: Cursor? = null
        try{
            ourCursor = ourDB.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            ourDB.execSQL(selectQuery)
            return ArrayList()
        }

        var carNameReturned: String
        var carModelReturned: String
        var durationReturned: Double
        var speedReturned: Double
        var distReturned: Double

        var plusGasReturned: Double
        var usedGasReturned: Double
        var currGasReturned: Double

        //fetch all the records until all are finished
        if (ourCursor.moveToFirst()) {
            do {
                //assign the values gotten to the respective strings
                carNameReturned = ourCursor.getString(ourCursor.getColumnIndex("name"))
                carModelReturned = ourCursor.getString(ourCursor.getColumnIndex("model"))
                durationReturned = ourCursor.getDouble(ourCursor.getColumnIndex("duration"))
                speedReturned = ourCursor.getDouble(ourCursor.getColumnIndex("ave_speed"))
                distReturned = ourCursor.getDouble(ourCursor.getColumnIndex("distance"))

                plusGasReturned = ourCursor.getDouble(ourCursor.getColumnIndex("plus_gas"))
                usedGasReturned = ourCursor.getDouble(ourCursor.getColumnIndex("used_gas"))
                currGasReturned = ourCursor.getDouble(ourCursor.getColumnIndex("curr_gas"))


                //add the values to the Model class and later to the arraylist
                //val carRow = CarModel(carName=carNameReturned, carModel = carModelReturned, duration = durationReturned, speed = speedReturned, dist = distReturned, plusGas = plusGasReturned, usedGas = usedGasReturned)
                val carRow = CarModel(carNameReturned, carModelReturned, durationReturned, speedReturned,  distReturned,  plusGasReturned, usedGasReturned)
                carList.add(carRow)

            } while (ourCursor.moveToNext())
        }
        return carList
    }
}