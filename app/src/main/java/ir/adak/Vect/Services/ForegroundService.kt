package ir.adak.Vect.Services

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.os.Build.VERSION_CODES.O
import android.os.Build
import androidx.core.app.NotificationCompat
import ir.adak.Vect.UI.Activities.MainActivity.MainActivity
import ir.adak.Vect.R


class ForegroundService : Service() {
    private val NOTIFICATION_ID: Int = 123
    private val CHANNEL_ID: String = "ForegroundChannelId"


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }





    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )


        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Foreground Service")
            .setSound(null)
            .setSmallIcon(R.drawable.img_avatar)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(NOTIFICATION_ID, notification)



        return START_REDELIVER_INTENT
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            serviceChannel.setSound(null, null)

            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

}