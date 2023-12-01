package com.github.oldmegit.goframeidea.provider

import com.github.oldmegit.goframeidea.golang.GoMod
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.psi.PsiElement

abstract class GfProvider: CompletionProvider<CompletionParameters>() {
    fun isGf(position: PsiElement): Boolean {
        val project = position.project
        return GoMod.isGf(project)
    }

    // check if the file is in the legal dir
    abstract fun isLegalFolder():Boolean
}