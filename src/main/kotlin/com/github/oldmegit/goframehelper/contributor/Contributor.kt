package com.github.oldmegit.goframehelper.contributor

import com.github.oldmegit.goframehelper.provider.ApiTagProvider
import com.github.oldmegit.goframehelper.provider.CallProvider
import com.goide.GoTypes
import com.goide.psi.GoCallExpr
import com.goide.psi.GoTag
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns

class Contributor: CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(GoTypes.RAW_STRING).withSuperParent(2, GoTag::class.java),
            ApiTagProvider()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(GoTypes.STRING).withSuperParent(3, GoCallExpr::class.java),
            CallProvider()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(GoTypes.RAW_STRING).withSuperParent(3, GoCallExpr::class.java),
            CallProvider()
        )
    }
}