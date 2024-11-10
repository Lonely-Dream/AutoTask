package com.example.autotask

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 设置 AlarmManager
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent1 = Intent(this, CoreTaskBroadcastReceiver::class.java)
        val alarmIntent2 = Intent(this, CoreTaskBroadcastReceiver::class.java)

        val pendingIntent1 = PendingIntent.getBroadcast(this, 0, alarmIntent1, PendingIntent.FLAG_UPDATE_CURRENT)
        val pendingIntent2 = PendingIntent.getBroadcast(this, 1, alarmIntent2, PendingIntent.FLAG_UPDATE_CURRENT)

        // 设置每天 8:45 的定时任务
        val calendar1 = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 45)
            set(Calendar.SECOND, 0)
        }

        // 设置每天 18:15 的定时任务
        val calendar2 = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 15)
            set(Calendar.SECOND, 0)
        }

        // 如果当前时间已超过设定的时间，设置为第二天
        if (calendar1.before(Calendar.getInstance())) {
            calendar1.add(Calendar.DAY_OF_YEAR, 1)
        }
        if (calendar2.before(Calendar.getInstance())) {
            calendar2.add(Calendar.DAY_OF_YEAR, 1)
        }

        // 设置定时任务
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar1.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent1
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar2.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent2
        )

        setContent {
            Greeting("Android")
        }
        Log.d("onCreate","初始化完成")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.height(50.dp))
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
