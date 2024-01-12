package com.github.oldmegit.goframehelper.callUtil.i18n.types

import com.github.oldmegit.goframehelper.callUtil.cfg.types.I18nType
import com.intellij.json.psi.JsonArray
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

object Json : I18nType {
    override fun getFileKeyValue(file: PsiElement): Map<String, PsiElement?> {
        val data = hashMapOf<String, PsiElement?>()
        val document = file.firstChild
        val all = PsiTreeUtil.findChildrenOfType(document, JsonProperty::class.java)

        var k: String
        var v: PsiElement?

        for (one in all) {
            k = one.getKey()
            v = null
            if (one.value !is JsonObject && one.value !is JsonArray) {
                v = one
            }
            data[k] = v
        }

        return data
    }

    private fun JsonProperty.getKey(): String {
        return this.firstChild.text.trim('"')
    }
}