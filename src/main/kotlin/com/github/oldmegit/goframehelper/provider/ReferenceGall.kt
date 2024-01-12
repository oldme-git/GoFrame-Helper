// YamlReference.kt
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
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YamlRecursivePsiElementVisitor

class ReferenceGall(element: PsiElement, range: TextRange, private val name: String) : PsiReferenceBase<PsiElement>(element, range) {
    override fun resolve(): PsiElement? {
        val callUtil = getCallUtil()
//        if (callUtil == null) {
//            return null
//        }
//        val data = callUtil.getData(element)

        // 查找所有的yaml文件，寻找匹配的键值对
        val project = element.project
        val yamlFiles = findYamlFiles(project)
        for (file in yamlFiles) {
            val psiFile = PsiManager.getInstance(project).findFile(file) ?: continue
            val keyValue = findKeyValueByName(psiFile, name) ?: continue
            return keyValue
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

    // 查找项目中的所有yaml文件
    private fun findYamlFiles(project: Project): List<VirtualFile> {
        val root = ProjectRootManager.getInstance(project).contentRoots.firstOrNull() ?: return emptyList()
        val files = mutableListOf<VirtualFile>()
//        root.refresh(false, true)
        val c = root.children
        for (file in c) {
            if (file.extension?.lowercase() == "yaml") {
                files.add(file)
            }
        }

        return files
    }

    // 查找yaml文件中的所有键值对
    private fun findAllKeyValues(psiFile: PsiElement): List<YAMLKeyValue> {
        val keyValues = mutableListOf<YAMLKeyValue>()
        psiFile.acceptChildren(object : YamlRecursivePsiElementVisitor() {
            override fun visitKeyValue(keyValue: YAMLKeyValue) {
                keyValues.add(keyValue)
                super.visitKeyValue(keyValue)
            }
        })
        return keyValues
    }

    // 查找yaml文件中的指定名称的键值对
    private fun findKeyValueByName(psiFile: PsiElement, name: String): YAMLKeyValue? {
        val keyValues = findAllKeyValues(psiFile)
        for (keyValue in keyValues) {
            if (keyValue.keyText == name) {
                return keyValue
            }
        }
        return null
    }
}
