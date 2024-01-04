package com.github.oldmegit.goframehelper.gf

import com.github.oldmegit.goframehelper.ui.AppSettingsState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
import java.nio.file.Paths

object Gf {
    val goModMark = "github.com/gogf/gf/v2"
    val icon = IconLoader.getIcon("/icons/gf16.svg", Gf::class.java)

    val openApiTagGMeta = mapOf(
        "path" to "",
        "tags" to "",
        "method" to "GET/PUT/POST/DELETE...",
        "deprecated" to "",
        "mime" to "default:application/json",
    )
    val openApiTagNormal = mapOf(
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
        "in" to "header/path/query/cookie",
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
        val settings = AppSettingsState.getInstance(project)
        return relative.startsWith(settings.gfApiDir + "/")
    }

    // check if it's gf logic file
    fun isLogicFile(project: Project, virtualFile: VirtualFile): Boolean {
        val relative = getRelativePath(project, virtualFile)
        // get api path setting
        val settings = AppSettingsState.getInstance(project)
        return relative.startsWith(settings.gfLogicDir + "/")
    }

    fun enableApiWatch(project: Project): Boolean {
        val settings = AppSettingsState.getInstance(project)
        return settings.gfEnableApiWatch
    }

    fun enableLogicWatch(project: Project): Boolean {
        val settings = AppSettingsState.getInstance(project)
        return settings.gfEnableLogicWatch
    }
    
    // get gf gen ctrl command
    fun gfGenCtrl(project: Project) : String {
        val settings = AppSettingsState.getInstance(project)
        return settings.gfCustomGfCli + " gen ctrl"
    }

    // get gf gen ctrl command
    fun gfGenService(project: Project) : String {
        val settings = AppSettingsState.getInstance(project)
        return settings.gfCustomGfCli + " gen service"
    }
}
