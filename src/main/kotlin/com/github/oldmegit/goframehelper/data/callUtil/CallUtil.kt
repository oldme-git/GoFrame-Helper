package com.github.oldmegit.goframehelper.data.callUtil

import com.intellij.psi.PsiElement

abstract class CallUtil {
    abstract fun getData(psiElement: PsiElement) : Map<String, Set<PsiElement>>
    // get completion of tail from psi
    abstract fun getPsiTail(psiElement: PsiElement?) : String

    // get completion of tail from set<psi>
    fun getPsiTail(setPsi: Set<PsiElement>): String {
        if (setPsi.size == 1) {
            return getPsiTail(setPsi.elementAt(0))
        }
        return ""
    }

    // merge map for same key
    protected fun Map<String, Set<PsiElement>>.merge(oneMap: Map<String, PsiElement?>) : Map<String, Set<PsiElement>> {
        val data = this.toMutableMap()

        for ((k, v) in oneMap) {
            val oldV = data.getOrElse(k) { mutableSetOf() }.toMutableSet()
            if (v != null) {
                oldV += v
            }
            data[k] = oldV
        }

        return data
    }
}
