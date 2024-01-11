package com.github.oldmegit.goframehelper.callUtil.cfg.types

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalar

object Yaml : CfgType {
    override fun getFileKeyValue(file: PsiElement): Map<String, PsiElement?> {
        val data: MutableMap<String, PsiElement?> = hashMapOf()

        val document = file.firstChild
        val all = PsiTreeUtil.findChildrenOfType(document, YAMLKeyValue::class.java)
        var k: String
        var v: PsiElement?

        for (one in all) {
            k = getParentKeys(one) + one.keyText
            v = null
            if (one.value is YAMLScalar) {
                v = one
            }
            data[k] = v
        }
        return data
    }

    override fun getParentKeys(psiElement: PsiElement): String {
        val parent = PsiTreeUtil.getParentOfType(psiElement, YAMLKeyValue::class.java)
        if (parent != null) {
            return getParentKeys(parent) + parent.keyText + "."
        }
        return ""
    }

    override fun getPsiTail(psiElement: PsiElement): String {
        var ctx = ""
        if (psiElement is YAMLKeyValue) {
            ctx = psiElement.valueText
        }
        return ctx
    }
}