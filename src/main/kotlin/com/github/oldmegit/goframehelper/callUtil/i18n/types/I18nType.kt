package com.github.oldmegit.goframehelper.callUtil.cfg.types

import com.intellij.psi.PsiElement

interface I18nType {
    fun getFileKey(file: PsiElement): Set<String>
}