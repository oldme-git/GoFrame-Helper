package com.github.oldmegit.goframeidea.listener

import com.github.oldmegit.goframeidea.utils.*
import com.goide.GoFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent


class ApiFile(private val project: Project): BulkFileListener {
    override fun after(events: MutableList<out VFileEvent>) {
        for (event in events) {
            val file = event.file
            if (event.isFromSave
                && file != null
                && file.isValid
                && file.path.endsWith('.' + GoFileType.DEFAULT_EXTENSION)
                && isGf(project)
                && isApiFile(project, file)) {
                // TODO
            }
        }
    }
}