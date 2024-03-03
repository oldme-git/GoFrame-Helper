package com.github.oldmegit.goframehelper.data.callUtil.cfg.types

import com.intellij.psi.PsiElement

interface CfgType {
    fun getFileKeyValue(file: PsiElement): Map<String, PsiElement?>
    fun getParentKeys(psiElement: PsiElement): String
    fun getPsiTail(psiElement: PsiElement) : String
}