package com.example.pr25_kargin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button
import android.widget.TextView;


class MainActivity : AppCompatActivity() {
    private lateinit var  tvTask1: TextView
    private lateinit var  tvTask2: TextView
    private lateinit var  tvTask3: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvTask1 = findViewById(R.id.tvTask1)
        tvTask2 = findViewById(R.id.tvTask2)
        tvTask3 = findViewById(R.id.tvTask3)
        findViewById<Button>(R.id.btnStart).setOnClickListener {
            pForTask(TASK1_CODE, 1000)
            pForTask(TASK2_CODE, 3000)
            pForTask(TASK3_CODE, 6000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == MyService.STATUS_START)
        {
            when (requestCode){
                TASK1_CODE -> tvTask1.text = "task1 start"
                TASK2_CODE -> tvTask2.text = "task2 start"
                TASK3_CODE -> tvTask3.text = "task3 start"
            }
        }
        else
        {
            val result : Long = data!!.getLongExtra(MyService.PARAM_RESULT,0)
            when (requestCode){
                TASK1_CODE -> tvTask1.text = "task1 finish ${result}"
                TASK2_CODE -> tvTask2.text = "task2 finish ${result}"
                TASK3_CODE -> tvTask3.text = "task3 finish ${result}"
            }
        }
    }
    private fun pForTask(taskCode : Int, time : Long){
        val pendingIntent = createPendingResult(taskCode, Intent(), 0)
        val intent = Intent(this, MyService::class.java)
        intent.putExtra(MyService.PARAM_P_INTENT, pendingIntent)
        intent.putExtra(MyService.PARAM_TIME, time)
        startService(intent)
    }
    companion object{
        const val TASK1_CODE = 1
        const val TASK2_CODE = 2
        const val TASK3_CODE = 3
    }
}