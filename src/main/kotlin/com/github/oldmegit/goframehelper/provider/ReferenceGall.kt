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
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil

class ReferenceGall(element: PsiElement, private val name: String) : PsiReferenceBase<PsiElement>(element), PsiPolyVariantReference {
    override fun multiResolve(boolean: Boolean): Array<ResolveResult> {
        val callUtil = getCallUtil()
        if (callUtil == null) {
            return arrayOf()
        }
        val data = callUtil.getData(element)
        var results: MutableList<ResolveResult> = ArrayList()

        for ((k, v) in data) {
            if (k == name) {
                results = addResult(v)
            }
        }
        return results.toTypedArray<ResolveResult>()
    }

    override fun resolve(): PsiElement? {
        val resolveResults = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

    override fun getVariants(): Array<LookupElement> {
        val variants = mutableListOf<LookupElement>()
        val callUtil = getCallUtil()
        if (callUtil == null) {
            return variants.toTypedArray()
        }
        val data = callUtil.getData(element)

        for ((k, v) in data) {
            val tail = callUtil.getPsiTail(v)
            variants.add(
                LookupElementBuilder.create(k)
                    .withIcon(Gf.icon)
                    .withTailText(" $tail", true)
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

    // add one PsiElement to results
    private fun addResult(setPsi: Set<PsiElement>) : MutableList<ResolveResult> {
        val results: MutableList<ResolveResult> = ArrayList()
        for (v in setPsi) {
            results.add(PsiElementResolveResult(v))
        }
        return results
    }
}
