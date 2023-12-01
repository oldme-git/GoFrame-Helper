package com.github.oldmegit.goframeidea.golang

import com.github.oldmegit.goframeidea.ui.AppSettingsState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
import java.nio.file.Paths

object Gf {
    val goModMark = "github.com/gogf/gf/v2"
    val icon = IconLoader.getIcon("/icons/gf16.png", Gf::class.java)

    val gfGenCtrl = "gf gen ctrl"
    val gfGenService = "gf gen service"

    val openApiTagGMeta = mapOf(
        "path" to "",
        "tags" to "",
        "method" to "GET/PUT/POST/DELETE...",
        "deprecated" to "",
        "mime" to "default:application/json",
    )
    val openApiTagNorMal = mapOf(
        "v" to "validate(only Req)",
    )
    val openApiTag = mapOf(
        "sm" to "summary",
        "summary" to "",
        "dc" to "description",
        "description" to "",
        "d" to "default",
        "default" to "",
        "type" to "",
    )

    // get project relative path
    private fun getRelativePath(project: Project, virtualFile: VirtualFile): String {
        val basePathCls = Paths.get(project.basePath.toString())
        val filePathCls = Paths.get(virtualFile.path)
        var absolute = basePathCls.relativize(filePathCls).toString()
        if (File.separator == "\\") {
            absolute = absolute.replace("\\", "/")
        }
        return absolute
    }

    // check if it's gf api file
    fun isApiFile(project: Project, virtualFile: VirtualFile): Boolean {
        val relative = getRelativePath(project, virtualFile)
        // get api path setting
        val settings = AppSettingsState.getInstance()
        return relative.startsWith(settings.gfApiDir + "/")
    }

    // check if it's gf logic file
    fun isLogicFile(project: Project, virtualFile: VirtualFile): Boolean {
        val relative = getRelativePath(project, virtualFile)
        // get api path setting
        val settings = AppSettingsState.getInstance()
        return relative.startsWith(settings.gfLogicDir + "/")
    }
}
