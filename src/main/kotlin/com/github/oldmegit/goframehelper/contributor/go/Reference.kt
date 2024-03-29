package com.github.oldmegit.goframehelper.contributor.go

import com.github.oldmegit.goframehelper.provider.go.ReferenceCallBase
import com.goide.psi.GoCallExpr
import com.goide.psi.GoStringLiteral
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*

class Reference : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(GoStringLiteral::class.java).withSuperParent(2, GoCallExpr::class.java),
            ReferenceCallBase()
        )
    }
}