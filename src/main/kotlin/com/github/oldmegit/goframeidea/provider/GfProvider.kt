package com.github.oldmegit.goframeidea.provider

import com.github.oldmegit.goframeidea.gf.GfGoMod
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext

abstract class GfProvider: CompletionProvider<CompletionParameters>() {
    protected lateinit var parameters: CompletionParameters
    protected lateinit var position: PsiElement
    protected lateinit var context: ProcessingContext
    protected lateinit var result: CompletionResultSet

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        this.parameters = parameters
        this.position = parameters.position
        this.context = context
        this.result = result

        if (!isGf()) {
            return
        }

        // check if the file is in the api folder
        if (!isValidFolder()) {
            return
        }

        addCompletionsEvent()
    }

    private fun isGf(): Boolean {
        val project = position.project
        return GfGoMod.isGf(project)
    }

    // addCompletions Event
    abstract fun addCompletionsEvent()

    // check if the file is in the legal dir
    abstract fun isValidFolder(): Boolean
}