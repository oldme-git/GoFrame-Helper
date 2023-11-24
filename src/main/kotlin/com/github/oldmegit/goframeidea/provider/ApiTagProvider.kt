package com.github.oldmegit.goframeidea.provider

import com.github.oldmegit.goframeidea.utils.isApi
import com.goide.psi.GoFile
import com.goide.psi.GoStructType
import com.goide.psi.GoTag
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext

class ApiTagProvider: CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val position = parameters.position

        val project = position.project
        val basePath = project.basePath
        val filePath = parameters.originalFile.virtualFile.path

        // check file in api
        if (!isApi(basePath.toString(), filePath)) {
            return
        }

        // check position in tag
        if (!checkTag(position)) {
            return
        }

        result.addElement(LookupElementBuilder.create("hello1"))
        result.addElement(LookupElementBuilder.create("hello2"))
    }

    private fun checkTag(position: PsiElement): Boolean {
        // check position in struct
        val struct = PsiTreeUtil.findFirstParent(
            position
        ) { e: PsiElement? -> e is GoStructType } as GoStructType?

        if (struct == null) {
            return false
        }

        // check position in struct tag
        val tag = PsiTreeUtil.findFirstParent(
            position
        ) { e: PsiElement? -> e is GoTag } as GoTag?

        if (tag == null) {
            return false
        }

        return true
    }
}