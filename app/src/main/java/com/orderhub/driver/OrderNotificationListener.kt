package com.orderhub.driver

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderNotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn == null) return

        val packageName = sbn.packageName
        val extras = sbn.notification.extras
        val title = extras.getString("android.title") ?: ""
        val text = extras.getString("android.text") ?: ""
        val fullText = "$title $text"

        var platform = ""
        if (packageName.contains("grab", true) || title.contains("grab", true)) {
            platform = "GRAB"
        } else if (packageName.contains("shopee", true) || title.contains("shopee", true)) {
            platform = "SHOPEE"
        } else if (packageName.contains("indrive", true) || title.contains("indrive", true)) {
            platform = "INDRIVE"
        }

        if (platform.isNotEmpty()) {
            val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            val nominal = if (fullText.contains("Rp")) {
                val sub = fullText.substringAfter("Rp").split(" ")[0]
                "Rp $sub"
            } else "Cek Detail"

            val dbHelper = OrderDatabaseHelper(this)
            dbHelper.insertOrder(platform, nominal, title, text, timeNow, fullText)

            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(500)
            }
        }
    }
}
