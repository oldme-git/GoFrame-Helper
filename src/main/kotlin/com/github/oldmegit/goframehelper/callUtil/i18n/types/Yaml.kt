package com.github.oldmegit.goframehelper.callUtil.i18n.types

import com.github.oldmegit.goframehelper.callUtil.cfg.types.I18nType
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalar

object Yaml : I18nType {
    override fun getFileKeyValue(file: PsiElement): Map<String, PsiElement?> {
        val data = hashMapOf<String, PsiElement?>()

        val document = file.firstChild
        val all = PsiTreeUtil.findChildrenOfType(document, YAMLKeyValue::class.java)
        var k: String
        var v: PsiElement?

        for (one in all) {
            k = one.keyText
            v = null
            if (one.value is YAMLScalar) {
                v = one
            }
            data[k] = v
        }
        return data
    }
}