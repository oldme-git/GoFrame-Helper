package com.github.oldmegit.goframehelper.callUtil.i18n.types

import com.github.oldmegit.goframehelper.callUtil.cfg.types.I18nType
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

object Json : I18nType {
    override fun getFileKeyValue(file: PsiElement): Map<String, PsiElement?> {
        val data = hashMapOf<String, PsiElement?>()
        val document = file.firstChild
        val all = PsiTreeUtil.findChildrenOfType(document, JsonProperty::class.java)

        var k: String

        for (one in all) {
            k = one.getKey()
            data[k] = one
        }

        return data
    }

    private fun JsonProperty.getKey(): String {
        return this.firstChild.text.trim('"')
    }
}