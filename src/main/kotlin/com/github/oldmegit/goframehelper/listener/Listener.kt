package com.github.oldmegit.goframehelper.listener

import com.github.oldmegit.goframehelper.data.Bundle
import com.github.oldmegit.goframehelper.gf.Gf
import com.github.oldmegit.goframehelper.gf.GfGoMod
import com.github.oldmegit.goframehelper.ui.AppSettingsState
import com.goide.GoFileType
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import java.io.File
import java.util.*


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

            try {
                var command = if (Gf.isApiFile(project, file) && Gf.enableApiWatch(project)) {
                    Gf.gfGenCtrl(project)
                } else if (Gf.isLogicFile(project, file) && Gf.enableLogicWatch(project)) {
                    Gf.gfGenService(project)
                } else {
                    return
                }

                val os = System.getProperty("os.name").lowercase(Locale.getDefault())
                if (os.contains("windows")) {
                    command = "cmd /c $command"
                } else if (os.contains("linux") || os.contains("mac")) {
                    command = "/bin/sh -c $command"
                }

                val process = Runtime.getRuntime().exec(command, null, File(project.basePath.toString()))
                val code = process.waitFor()
                if (code != 0) {
                    throw Exception("execute command fail")
                }
            } catch (_: Exception) {
                val message = Bundle.getMessage("gfExecErrNotify")
                val settings = AppSettingsState.getInstance(project)
                settings.gfEnableApiWatch = false
                settings.gfEnableLogicWatch = false

                NotificationGroupManager.getInstance()
                    .getNotificationGroup("GoFrame Help Notify")
                    .createNotification(message, NotificationType.INFORMATION)
                    .notify(project)
            }
        }
    }
}