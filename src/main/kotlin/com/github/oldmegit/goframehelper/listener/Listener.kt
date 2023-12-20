package com.github.oldmegit.goframehelper.listener

import com.github.oldmegit.goframehelper.data.Bundle
import com.github.oldmegit.goframehelper.gf.Gf
import com.github.oldmegit.goframehelper.gf.GfGoMod
import com.goide.GoFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import java.io.File

class Listener(private val project: Project): BulkFileListener {
    override fun after(events: MutableList<out VFileEvent>) {
        for (event in events) {
            val file = event.file ?: return

            // listen go.mod change
            if (file.path.endsWith("go.mod")) {
                GfGoMod.reset(project)
                return
            }

            if (!(event.isFromSave
                    && file.isValid
                    && file.path.endsWith('.' + GoFileType.DEFAULT_EXTENSION))) {
                return
            }

            val runtime = Runtime.getRuntime()
            try {
                if (Gf.isApiFile(project, file) && Gf.enableApiWatch(project)) {
                    runtime.exec(Gf.gfGenCtrl(project), null, File(project.basePath.toString()))
                } else if (Gf.isLogicFile(project, file) && Gf.enableLogicWatch(project)) {
                    runtime.exec(Gf.gfGenService(project), null, File(project.basePath.toString()))
                }
            } catch (_: Exception) {
                val message = Bundle.getMessage("gfExecErrNotify")
                NotificationGroupManager.getInstance()
                    .getNotificationGroup("GoFrame Help Notify")
                    .createNotification(message, NotificationType.INFORMATION)
                    .notify(project)
            }
        }
    }
}