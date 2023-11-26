package com.github.oldmegit.goframeidea.contributor

import com.github.oldmegit.goframeidea.provider.ApiTagProvider
import com.goide.GoTypes
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
    }
}