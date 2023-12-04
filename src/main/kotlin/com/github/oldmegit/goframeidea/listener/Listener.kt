package com.github.oldmegit.goframeidea.listener

import com.github.oldmegit.goframeidea.goFrame.Gf
import com.github.oldmegit.goframeidea.goFrame.GfGoMod
import com.goide.GoFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
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
            if (Gf.isApiFile(project, file) && Gf.enableApiWatch(project)) {
                runtime.exec(Gf.gfGenCtrl, null, File(project.basePath.toString()))
            } else if (Gf.isLogicFile(project, file) && Gf.enableLogicWatch(project)) {
                runtime.exec(Gf.gfGenService, null, File(project.basePath.toString()))
            }
        }
    }
}