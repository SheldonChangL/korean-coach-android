package com.koreancoach.app.data.notification

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val TAG = "korean_coach_reminder"
    }

    override suspend fun doWork(): Result {
        NotificationHelper.showReminder(applicationContext)
        return Result.success()
    }
}
