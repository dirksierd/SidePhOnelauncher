package app.sidephonelauncher.helper

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class HomeNotificationListenerService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        NotificationDotRepository.update(activeNotifications)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        NotificationDotRepository.update(activeNotifications)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        NotificationDotRepository.update(activeNotifications)
    }

    override fun onListenerDisconnected() {
        NotificationDotRepository.clear()
        super.onListenerDisconnected()
    }
}
