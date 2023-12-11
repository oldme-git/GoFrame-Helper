package com.github.oldmegit.goframeidea.callUtil.cfg.types

import com.intellij.psi.PsiElement

interface CfgType {
    fun getFileKeyValue(file: PsiElement): Map<String, String>
    fun getParentKeys(psiElement: PsiElement): String
}