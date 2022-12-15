package com.example.pr25_kargin

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class MyService : Service() {
    private val executorService: ExecutorService = Executors.newFixedThreadPool(N_THREADS)
    private var runningTaskCount : AtomicInteger = AtomicInteger(0)
    override fun onBind(intent: Intent): IBinder {
        return null!!
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val timeMs = intent.getLongExtra(PARAM_TIME, 1000)
        val pendingIntent = intent.getParcelableExtra<PendingIntent>(PARAM_P_INTENT)!!
        executorService.execute { runLongTask(timeMs, pendingIntent) };
        return START_REDELIVER_INTENT
    }

    private fun runLongTask(timeMs: Long, pendingIntent: PendingIntent) {
        runningTaskCount.incrementAndGet()
        pendingIntent.send(STATUS_START)
        Thread.sleep(timeMs)
        val intent = Intent().putExtra(PARAM_RESULT, timeMs)
        pendingIntent.send(this, STATUS_FINISH, intent)
        if (runningTaskCount.decrementAndGet() == 0) {
            stopSelf()
        }
    }

    companion object{
        const val STATUS_START = 1
        const val STATUS_FINISH = 2
        const val PARAM_TIME = "1"
        const val PARAM_P_INTENT = "2"
        const val PARAM_RESULT = "3"
        const val N_THREADS = 4
    }

}