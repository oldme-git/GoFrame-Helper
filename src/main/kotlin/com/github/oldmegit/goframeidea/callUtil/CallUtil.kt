package com.github.oldmegit.goframeidea.callUtil

import com.intellij.psi.PsiElement

interface CallUtil {
    fun getData(psiElement: PsiElement): Map<String, String>
}