package com.example.myapplication.util

import android.util.Log
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService

/**
 * Created by HyelimKim on 2017-12-16.
 */
class CustomJobService : JobService() {
    val TAG = "CustomJobService"

    override fun onStopJob(job: JobParameters?): Boolean {
        Log.d(TAG, "Performing long running task in scheduled job ${job.toString()}")
        return false
    }

    override fun onStartJob(job: JobParameters?): Boolean {
        Log.d(TAG, "Performing long running task in scheduled job ${job.toString()}")

        return false
    }

}