package com.github.oldmegit.goframehelper.completion.go

import com.github.oldmegit.goframehelper.data.callUtil.orm.OrmCallables
import com.github.oldmegit.goframehelper.data.callUtil.orm.OrmUtil
import com.github.oldmegit.goframehelper.gf.Gf
import com.goide.psi.GoCallExpr
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class CompletionCall : CompletionBase() {
    override fun addCompletionsEvent() {
        val call = PsiTreeUtil.findFirstParent(this.position) { e: PsiElement ->
            e is GoCallExpr
        } as GoCallExpr

        val callUtil =
            if (OrmCallables.find(call, false) != null) {
                OrmUtil
            } else {
                return
            }

        val data = callUtil.getData(position)
        for ((k, v) in data) {
            val tail = callUtil.getPsiTail(v)
            result.addElement(
                LookupElementBuilder.create(k)
                    .withIcon(Gf.icon)
                    .withTailText(" $tail", true)
            )
        }
    }

    override fun isValidFolder(): Boolean {
        return true
    }
}