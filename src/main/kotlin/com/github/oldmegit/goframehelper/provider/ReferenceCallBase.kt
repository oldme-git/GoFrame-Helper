package com.github.oldmegit.goframehelper.provider

import com.goide.psi.GoStringLiteral
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext

class ReferenceCallBase : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        if (element is GoStringLiteral) {
            var text = element.text
            text = text.trim('"')
            return arrayOf(ReferenceGall(element, text))
        }
        return PsiReference.EMPTY_ARRAY
    }
}