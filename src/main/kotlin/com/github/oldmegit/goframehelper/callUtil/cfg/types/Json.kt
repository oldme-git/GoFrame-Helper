package com.github.oldmegit.goframehelper.callUtil.cfg.types

import com.intellij.json.psi.JsonArray
import com.intellij.json.psi.JsonElement
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.yaml.psi.YAMLKeyValue

object Json : CfgType {
    override fun getFileKeyValue(file: PsiElement): Map<String, PsiElement?> {
        val data: MutableMap<String, PsiElement?> = hashMapOf()

        val document = file.firstChild
        val all = PsiTreeUtil.findChildrenOfType(document, JsonProperty::class.java)
        var k: String
        var v: PsiElement?

        for (one in all) {
            k = getParentKeys(one) + one.getKey()
            v = null
            if (one.value !is JsonObject && one.value !is JsonArray) {
                v = one
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

    override fun getPsiTail(psiElement: PsiElement): String {
        var ctx = ""
        if (psiElement !is JsonProperty) {
            return ctx
        }
        if (psiElement.value !is JsonObject && psiElement.value !is JsonArray) {
            ctx = psiElement.value?.text!!.trim('"')
        }
        return ctx
    }

    private fun JsonProperty.getKey(): String {
        return this.firstChild.text.trim('"')
    }
}