package com.example.autotask

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Build
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.util.Log

class CoreTaskBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("onReceive","开始执行")
        // 获取 PowerManager 来保持唤醒
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        // 请求一个 WakeLock，确保屏幕亮起并唤醒设备
        val wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "AutoTask:WakeLock"
        )

        // 获取 WakeLock，确保设备唤醒并屏幕亮起
        wakeLock.acquire(10 * 1000L /*10秒*/)

        // 发送通知
        sendNotification(context)

        // 执行任务：打开系统设置
        val launchIntent = context.packageManager.getLaunchIntentForPackage(TARGET_APP_PACKAGE_NAME)
        context.startActivity(launchIntent)

        // 任务完成后释放 WakeLock
        wakeLock.release()

        // 1分钟后打开另外一个app
        Handler(Looper.getMainLooper()).postDelayed({
            context.startActivity(
                context.packageManager.getLaunchIntentForPackage(SET_APP_PACKAGE_NAME))
        }, 1 * 60 * 1000L)

        Log.d("onReceive","执行结束")
    }

    private fun sendNotification(context: Context) {
        // 通知渠道 ID 和名称
        val channelId = "task_notification_channel"
        val channelName = "任务提醒通知"

        // 创建通知渠道（仅限 Android 8.0 以上系统）
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // 创建通知内容
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // 使用系统图标，或自定义图标
            .setContentTitle("自动任务提醒")
            .setContentText("任务即将执行，打开系统设置")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // 显示通知
        NotificationManagerCompat.from(context).notify(1, notification)
    }

    companion object{
        const val TARGET_APP_PACKAGE_NAME = "com.ss.android.lark"
        const val SET_APP_PACKAGE_NAME = "com.android.settings"
    }
}
