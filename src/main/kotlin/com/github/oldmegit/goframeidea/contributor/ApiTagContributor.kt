package com.github.oldmegit.goframeidea.contributor

import com.github.oldmegit.goframeidea.provider.ApiTagProvider
import com.goide.GoParserDefinition
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns

class ApiTagContributor: CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withElementType(GoParserDefinition.Lazy.STRING_LITERALS),
            ApiTagProvider()
        )
    }
}