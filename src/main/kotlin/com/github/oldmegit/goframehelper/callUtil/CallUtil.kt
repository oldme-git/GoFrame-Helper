package com.github.oldmegit.goframehelper.callUtil

import com.intellij.psi.PsiElement

interface CallUtil {
    fun getData(psiElement: PsiElement) : Map<String, PsiElement?>
    // get completion of tail from psi
    fun getPsiTail(psiElement: PsiElement?) : String
}