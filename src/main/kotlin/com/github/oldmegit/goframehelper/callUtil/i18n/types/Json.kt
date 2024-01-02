package com.github.oldmegit.goframehelper.callUtil.i18n.types

import com.github.oldmegit.goframehelper.callUtil.cfg.types.I18nType
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

object Json : I18nType {
    override fun getFileKey(file: PsiElement): Set<String> {
        val data = hashSetOf<String>()
        val document = file.firstChild
        val all = PsiTreeUtil.findChildrenOfType(document, JsonProperty::class.java)

        for (one in all) {
            data.add(one.getKey())
        }
        return data
    }

    private fun JsonProperty.getKey(): String {
        return this.firstChild.text.trim('"')
    }
}