package com.github.oldmegit.goframehelper.contributor

import com.github.oldmegit.goframehelper.provider.CallReferenceProvider
import com.goide.psi.GoCallExpr
import com.goide.psi.GoStringLiteral
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*

class Reference : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(GoStringLiteral::class.java).withSuperParent(2, GoCallExpr::class.java),
            CallReferenceProvider()
        )
    }
}