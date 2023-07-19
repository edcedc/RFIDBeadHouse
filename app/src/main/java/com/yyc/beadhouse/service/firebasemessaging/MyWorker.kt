package com.google.firebase.quickstart.fcm.kotlin

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.blankj.utilcode.util.LogUtils

class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        LogUtils.e("Performing long running task in scheduled job")
        return Result.success()
    }

}
