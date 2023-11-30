package com.github.oldmegit.goframeidea.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.psi.PsiElement
import com.github.oldmegit.goframeidea.utils.isGf

abstract class GfProvider: CompletionProvider<CompletionParameters>() {
    private var isGfCache: Boolean? = null

    // check if it's gf project
    fun isGf(position: PsiElement): Boolean {
//        if (isGfCache != null) {
//            return isGfCache as Boolean
//        }
        val project = position.project
        return isGf(project)
    }

    // check if the file is in the legal dir
    abstract fun isLegalFolder():Boolean
}