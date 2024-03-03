package com.github.oldmegit.goframehelper.data.callUtil.i18n.types

import com.intellij.psi.PsiElement

interface I18nType {
    fun getFileKeyValue(file: PsiElement): Map<String, PsiElement?>
}