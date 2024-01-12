package com.github.oldmegit.goframehelper.contributor

import com.github.oldmegit.goframehelper.provider.CompletionApiTag
import com.github.oldmegit.goframehelper.provider.CompletionCall
import com.goide.GoTypes
import com.goide.psi.GoCallExpr
import com.goide.psi.GoTag
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns

class Completion: CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(GoTypes.RAW_STRING).withSuperParent(2, GoTag::class.java),
            CompletionApiTag()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(GoTypes.STRING).withSuperParent(3, GoCallExpr::class.java),
            CompletionCall()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(GoTypes.RAW_STRING).withSuperParent(3, GoCallExpr::class.java),
            CompletionCall()
        )
    }
}