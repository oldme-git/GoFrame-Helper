package com.github.oldmegit.goframeidea.utils

import com.github.oldmegit.goframeidea.gf.Gf
import com.github.oldmegit.goframeidea.ui.AppSettingsState
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
import java.nio.file.Paths

// check if it's gf project
fun isGf(project: Project): Boolean {
    val basePath = project.basePath
    val goModFile = "$basePath/go.mod"

    val file = File(goModFile)

    if (file.exists() && file.isFile) {
        val fileContent = file.readText()
        return fileContent.contains(Gf.goModMark)
    }
    return false
}

fun isApiFile(project: Project, virtualFile: VirtualFile): Boolean {
    val basePathCls = Paths.get(project.basePath.toString())
    val filePathCls = Paths.get(virtualFile.path)
    var absolute = basePathCls.relativize(filePathCls).toString()
    if (File.separator == "\\") {
        absolute = absolute.replace("\\", "/")
    }
    val absolutePaths = absolute.split("/")
    // get api path setting
    val settings = AppSettingsState.getInstance()
    return absolutePaths[0] == settings.gfApiDir
}