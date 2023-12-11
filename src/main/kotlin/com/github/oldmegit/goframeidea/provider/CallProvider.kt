package com.github.oldmegit.goframeidea.provider

import com.github.oldmegit.goframeidea.callUtil.cfg.CfgCallables
import com.github.oldmegit.goframeidea.callUtil.cfg.CfgUtil
import com.github.oldmegit.goframeidea.callUtil.orm.OrmCallables
import com.github.oldmegit.goframeidea.callUtil.orm.OrmUtil
import com.github.oldmegit.goframeidea.gf.Gf
import com.goide.psi.GoCallExpr
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class CallProvider : GfProvider() {
    override fun addCompletionsEvent() {
        val call = PsiTreeUtil.findFirstParent(this.position) { e: PsiElement ->
            e is GoCallExpr
        } as GoCallExpr

        val callUtil =
            if (OrmCallables.find(call, false) != null) {
                OrmUtil
            } else if (CfgCallables.find(call, false) != null) {
                CfgUtil
            } else {
                return
            }

        val data = callUtil.getData(position)
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