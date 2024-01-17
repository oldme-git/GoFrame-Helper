package com.github.oldmegit.goframehelper.callUtil.i18n.types

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.yaml.psi.YAMLKeyValue

object Yaml : I18nType {
    override fun getFileKeyValue(file: PsiElement): Map<String, PsiElement?> {
        val data = hashMapOf<String, PsiElement?>()

        val document = file.firstChild
        val all = PsiTreeUtil.findChildrenOfType(document, YAMLKeyValue::class.java)
        var k: String

        for (one in all) {
            k = one.keyText
            data[k] = one
        }
        return data
    }
}