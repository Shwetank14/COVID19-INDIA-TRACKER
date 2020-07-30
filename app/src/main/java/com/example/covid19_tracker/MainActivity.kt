package com.example.covid19_tracker

import android.icu.util.TimeUnit
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var stateListAdapter: StateListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        listViewTv.addHeaderView(LayoutInflater
            .from(this).inflate(R.layout.items_of_head,listViewTv,false))
        fetchResult()
    }

    private fun fetchResult() {
        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) { Client.api.clone().execute() }
            if (response.isSuccessful){
                val data = Gson().fromJson(response.body?.string(),Response::class.java)
                launch(Dispatchers.Main) {
                    bindCombinedData(data.statewise[0])
                    bindStateData(data.statewise.subList(1,data.statewise.size))
                }
            }
        }
    }

    private fun bindStateData(subList: List<StatewiseItem>) {
            stateListAdapter = StateListAdapter(subList)
        listViewTv.adapter = stateListAdapter
    }

    private fun bindCombinedData(data: StatewiseItem) {
        val lastUpdatedTime = data.lastupdatedtime
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        updateTv.text = "Last Updated ${getTimeAgo(simpleDateFormat.parse(lastUpdatedTime))}"

        totalCasesTv.text  = data.confirmed
        recoverTv.text = data.recovered
        deathTv.text = data.deaths
        activeTv.text = data.active
    }

    private fun getTimeAgo(past: Date): String {
        val now = Date()
        val seconds = java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
        val minutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours = java.util.concurrent.TimeUnit.MILLISECONDS.toHours(now.time - past.time)

        return when {

            seconds < 60 -> {
                "Few seconds ago"
            }
            minutes < 60 ->{
                "$minutes minutes ago"
            }
            hours < 24 -> {
                "$hours hours ago"
            }
            else ->{
                SimpleDateFormat("dd/MM/yy, hh:mm a").format(past).toString()
            }

        }

    }


}