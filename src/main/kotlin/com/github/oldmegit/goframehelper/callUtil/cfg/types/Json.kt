package com.github.oldmegit.goframehelper.callUtil.cfg.types

import com.intellij.json.psi.JsonArray
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

object Json : CfgType {
    override fun getFileKeyValue(file: PsiElement): Map<String, String> {
        val data: MutableMap<String, String> = hashMapOf()

        val document = file.firstChild
        val all = PsiTreeUtil.findChildrenOfType(document, JsonProperty::class.java)
        var k: String
        var v = ""

        for (one in all) {
            k = getParentKeys(one) + one.getKey()
            if (one.value !is JsonObject && one.value !is JsonArray) {
                v = one.value?.text!!
            }
            data[k] = v
        }
        return data
    }

    override fun getParentKeys(psiElement: PsiElement): String {
        val parent = PsiTreeUtil.getParentOfType(psiElement, JsonProperty::class.java)
        if (parent != null) {
            return getParentKeys(parent) + parent.getKey() + "."
        }
        return ""
    }

    private fun JsonProperty.getKey(): String {
        return this.firstChild.text.trim('"')
    }
}