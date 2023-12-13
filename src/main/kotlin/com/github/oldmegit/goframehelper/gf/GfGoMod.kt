package com.github.oldmegit.goframehelper.gf

import com.github.oldmegit.goframehelper.data.Cache
import com.intellij.openapi.project.Project
import java.io.File

object GfGoMod {
    // check if it's gf project
    fun isGf(project: Project): Boolean {
        val cache = Cache.getInstance(project)
        if (cache.isGf != null) {
            return cache.isGf!!
        }
        val basePath = project.basePath
        val goModFile = "$basePath/go.mod"
        val file = File(goModFile)

        if (file.exists() && file.isFile) {
            val fileContent = file.readText()
            cache.isGf = fileContent.contains(Gf.goModMark)
        }
        return cache.isGf ?: false
    }

    // reset isGf cache
    fun reset(project: Project) {
        val cache = Cache.getInstance(project)
        cache.isGf = null
        isGf(project)
    }
}