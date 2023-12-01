package com.github.oldmegit.goframeidea.golang

import com.intellij.openapi.project.Project
import java.io.File

object GoMod {
    private var isGfCache: Boolean? = null

    // check if it's gf project
    fun isGf(project: Project): Boolean {
        if (isGfCache != null) {
            return isGfCache as Boolean
        }
        val basePath = project.basePath
        val goModFile = "$basePath/go.mod"
        val file = File(goModFile)

        if (file.exists() && file.isFile) {
            val fileContent = file.readText()
            isGfCache = fileContent.contains(Gf.goModMark)
        }
        return isGfCache as Boolean
    }

    fun changeEvent(project: Project) {
        isGfCache = null
        isGf(project)
    }
}