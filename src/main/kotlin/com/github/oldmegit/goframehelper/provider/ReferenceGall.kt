package com.github.oldmegit.goframehelper.provider

import com.github.oldmegit.goframehelper.callUtil.CallUtil
import com.github.oldmegit.goframehelper.callUtil.cfg.CfgCallables
import com.github.oldmegit.goframehelper.callUtil.cfg.CfgUtil
import com.github.oldmegit.goframehelper.callUtil.i18n.I18nCallables
import com.github.oldmegit.goframehelper.callUtil.i18n.I18nUtil
import com.github.oldmegit.goframehelper.gf.Gf
import com.goide.psi.GoCallExpr
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil

class ReferenceGall(element: PsiElement, range: TextRange, private val name: String) : PsiReferenceBase<PsiElement>(element, range) {
    override fun resolve(): PsiElement? {
        val callUtil = getCallUtil()
        if (callUtil == null) {
            return null
        }
        val data = callUtil.getData(element)

        for ((k, v) in data) {
            if (k == name && v != null) {
                return v
            }
        }
        return null
    }

    override fun getVariants(): Array<LookupElement> {
        val variants = mutableListOf<LookupElement>()
        val callUtil = getCallUtil()
        if (callUtil == null) {
            return variants.toTypedArray()
        }
        val data = callUtil.getData(element)

        for ((k, v) in data) {
            val vCtx = callUtil.getPsiTail(v)
            variants.add(
                LookupElementBuilder.create(k)
                    .withIcon(Gf.icon)
                    .withTailText(" $vCtx", true)
            )
        }

        return variants.toTypedArray()
    }

    private fun getCallUtil() : CallUtil? {
        val call = PsiTreeUtil.findFirstParent(element) { e: PsiElement ->
            e is GoCallExpr
        } as GoCallExpr
        var callUtil: CallUtil? = null

        if (CfgCallables.find(call, false) != null) {
            callUtil = CfgUtil
        } else if (I18nCallables.find(call, false) != null) {
            callUtil = I18nUtil
        }

        return callUtil
    }
}
