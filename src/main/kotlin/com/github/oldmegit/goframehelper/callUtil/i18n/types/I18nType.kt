package com.github.oldmegit.goframehelper.callUtil.cfg.types

import com.intellij.psi.PsiElement

interface I18nType {
    fun getFileKeyValue(file: PsiElement): Map<String, PsiElement?>
}