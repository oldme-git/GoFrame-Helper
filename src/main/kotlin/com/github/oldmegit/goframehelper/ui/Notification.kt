package com.github.oldmegit.goframehelper.ui

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object Notification {
    fun message(project: Project, message: String) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("GoFrame Help Notify")
            .createNotification(message, NotificationType.INFORMATION)
            .notify(project)
    }
}