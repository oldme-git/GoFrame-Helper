package com.github.oldmegit.goframeidea.provider

import com.github.oldmegit.goframeidea.goFrame.OrmCallables
import com.goide.psi.GoCallExpr
import com.goide.psi.impl.GoPsiUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class OrmProvider : GfProvider() {
    override fun addCompletionsEvent() {
        println(1)

        val call = PsiTreeUtil.findFirstParent(this.position) { e: PsiElement? ->
            e is GoCallExpr
        } as GoCallExpr? ?: return

        val descriptor = OrmCallables.find(call, false)
        println(descriptor == null)
    }

    override fun isValidFolder(): Boolean {
        return true
    }

    fun getCallName(call: GoCallExpr): String {
        val callRef = GoPsiUtil.getCallReference(call)
        return callRef?.identifier?.text.toString()
    }
}