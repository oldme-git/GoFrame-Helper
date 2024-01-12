package com.github.oldmegit.goframehelper.listener

import com.github.oldmegit.goframehelper.data.Bundle
import com.github.oldmegit.goframehelper.gf.Gf
import com.github.oldmegit.goframehelper.gf.GfGoMod
import com.github.oldmegit.goframehelper.ui.AppSettingsState
import com.github.oldmegit.goframehelper.ui.Notification
import com.goide.GoFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
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

            fileWatch(file)
        }
    }

    // file watch exec command
    private fun fileWatch(file: VirtualFile) {
        try {
            // get commandRaw
            val commandRaw = if (Gf.isApiFile(project, file) && Gf.enableApiWatch(project)) {
                Gf.gfGenCtrl(project)
            } else if (Gf.isLogicFile(project, file) && Gf.enableLogicWatch(project)) {
                Gf.gfGenService(project)
            } else {
                return
            }

            // exec command based on different systems
            val os = System.getProperty("os.name").lowercase(Locale.getDefault())
            val process = if (os.contains("windows")) {
                Runtime.getRuntime().exec(commandRaw, null, File(project.basePath.toString()))
            } else if (os.contains("linux") || os.contains("mac")) {
                val workDir = project.basePath
                val command = arrayOf("sh", "-c", "cd $workDir && $commandRaw")
                ProcessBuilder(*command).start()
            } else {
                Notification.message(project, Bundle.getMessage("fileWatch.NotSupport"))
                return
            }

            val code = process.waitFor()
            if (code != 0) {
                throw Exception(code.toString())
            }
        } catch (_: Exception) {
            val message = Bundle.getMessage("fileWatch.ExecErr")
            val settings = AppSettingsState.getInstance(project)
            settings.gfEnableApiWatch = false
            settings.gfEnableLogicWatch = false
            Notification.message(project, message)
        }
    }
}