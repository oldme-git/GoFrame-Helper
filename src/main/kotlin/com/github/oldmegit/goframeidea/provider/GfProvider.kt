package com.github.oldmegit.goframeidea.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.psi.PsiElement
import java.io.File

abstract class GfProvider: CompletionProvider<CompletionParameters>() {
    private var isGfCache: Boolean? = null

    // check if it's gf project
    fun isGf(position: PsiElement): Boolean {
//        if (isGfCache != null) {
//            return isGfCache as Boolean
//        }
        val project = position.project
        val basePath = project.basePath
        val goModFile = "$basePath/go.mod"

        val file = File(goModFile)

        if (file.exists() && file.isFile) {
            val fileContent = file.readText()
            isGfCache = fileContent.contains("github.com/gogf/gf/v2")
        }
        return isGfCache as Boolean
    }

    // check if the file is in the legal dir
    abstract fun isLegalFolder(folderPath: String, filePath: String):Boolean
}