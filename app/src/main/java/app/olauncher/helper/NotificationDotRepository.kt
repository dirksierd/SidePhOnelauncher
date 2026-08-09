package app.olauncher.helper

import android.app.Notification
import android.service.notification.StatusBarNotification
import java.util.concurrent.CopyOnWriteArraySet

object NotificationDotRepository {
    private val listeners = CopyOnWriteArraySet<() -> Unit>()

    @Volatile
    private var packagesWithNotifications: Set<String> = emptySet()

    fun addListener(listener: () -> Unit) {
        listeners += listener
    }

    fun removeListener(listener: () -> Unit) {
        listeners -= listener
    }

    fun hasActiveNotification(packageName: String): Boolean {
        if (packageName.isBlank()) return false
        return packageName in packagesWithNotifications
    }

    fun update(activeNotifications: Array<StatusBarNotification>?) {
        val nextPackages = activeNotifications
            ?.asSequence()
            ?.filterNot(::shouldIgnore)
            ?.map { it.packageName }
            ?.filter { it.isNotBlank() }
            ?.toSet()
            ?: emptySet()

        if (nextPackages == packagesWithNotifications) return
        packagesWithNotifications = nextPackages
        listeners.forEach { it() }
    }

    fun clear() {
        if (packagesWithNotifications.isEmpty()) return
        packagesWithNotifications = emptySet()
        listeners.forEach { it() }
    }

    private fun shouldIgnore(sbn: StatusBarNotification): Boolean {
        val notification = sbn.notification
        val flags = notification.flags
        val category = notification.category
        val isGroupSummary = flags and Notification.FLAG_GROUP_SUMMARY != 0
        val isForegroundService = flags and Notification.FLAG_FOREGROUND_SERVICE != 0
        val isOngoing = flags and Notification.FLAG_ONGOING_EVENT != 0
        val isPersistent = !sbn.isClearable
        val isTransport = category == Notification.CATEGORY_TRANSPORT
        val isService = category == Notification.CATEGORY_SERVICE
        return isGroupSummary || isForegroundService || isOngoing || isPersistent || isTransport || isService
    }
}
