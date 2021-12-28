package com.example.studytestapp.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.studytestapp.ListenerBackground
import com.example.studytestapp.MainActivity
import com.example.studytestapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    lateinit var type : NotificationType
    lateinit var message : String

    // 토큰이 갱신 될 때마다 이 곳에서 작업처리를 해주면 된다.
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    // 메시지 수신시마다 실행
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        createNotificationChannelIfNeeded()         // 채널 생성

        // 포그라운드 백그라운드에 따른 푸시 타입설정
        val title = remoteMessage.data["title"]
        if(ListenerBackground.isForeground) {
            type = NotificationType.NORMAL
            message = "onAppForegrounded"
        } else {
            type = NotificationType.CUSTOM
            message = "onAppBackgrounded"
        }

        NotificationManagerCompat.from(this)
            .notify(type.id, createNotification(type, title, message))
    }

    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 채널의 경우에는 버전 O부터 생성
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT // 중요도
            )
            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    private fun createNotification(type: NotificationType, title: String?, message: String?): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, type.id, intent, FLAG_UPDATE_CURRENT)
        // 실제 알림 컨텐츠 만들기
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24) // 아이콘 보여주기
            .setContentTitle(title) // 타이틀
            .setContentText(message) // 메시지 내용
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // 오레오 이하 버전에서는 지정 필요
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // 알림 클릭 시 자동 제거
        when (type) {

            NotificationType.NORMAL -> Unit

            //커스텀 알림
            NotificationType.CUSTOM -> {
                notificationBuilder
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(
                            packageName,
                            R.layout.view_custom_notification
                        ).apply {
                            setTextViewText(R.id.title, title)
                            setTextViewText(R.id.message, message)
                        }
                    )
            }
        }
        return notificationBuilder.build()
    }

    companion object {
        private const val CHANNEL_NAME = "channel_1"
        private const val CHANNEL_DESCRIPTION = "포그라운드 백그라운드에 따른 노티 알림"
        private const val CHANNEL_ID = "Channel Id"
    }
}