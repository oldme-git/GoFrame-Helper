package com.github.oldmegit.goframehelper.callUtil.i18n.types

import com.github.oldmegit.goframehelper.callUtil.cfg.types.I18nType
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.yaml.psi.YAMLKeyValue

object Yaml : I18nType {
    override fun getFileKey(file: PsiElement): Set<String> {
        val data = hashSetOf<String>()
        val document = file.firstChild
        val all = PsiTreeUtil.findChildrenOfType(document, YAMLKeyValue::class.java)

        for (one in all) {
            data.add(one.keyText)
        }
        return data
    }
}