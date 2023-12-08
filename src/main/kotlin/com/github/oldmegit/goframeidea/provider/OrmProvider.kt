package com.github.oldmegit.goframeidea.provider

import com.github.oldmegit.goframeidea.gf.Gf
import com.github.oldmegit.goframeidea.gf.OrmCallables
import com.github.oldmegit.goframeidea.gf.OrmUtil
import com.goide.psi.GoCallExpr
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class OrmProvider : GfProvider() {
    override fun addCompletionsEvent() {
        val call = PsiTreeUtil.findFirstParent(this.position) { e: PsiElement ->
            e is GoCallExpr
        } as GoCallExpr

        OrmCallables.conditionList.find(call, false) ?: return

        val data = OrmUtil.getData(position)
        for ((k, v) in data) {
            result.addElement(
                LookupElementBuilder.create(k)
                    .withIcon(Gf.icon)
                    .withTailText(" $v", true)
            )
        }
    }

    override fun isValidFolder(): Boolean {
        return true
    }
}