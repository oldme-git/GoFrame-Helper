// YamlReference.kt
package com.github.oldmegit.goframehelper.ddd

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveResult
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalar
import org.jetbrains.yaml.psi.YamlRecursivePsiElementVisitor

class YamlReference(element: YAMLScalar, range: TextRange, private val name: String) : PsiReferenceBase<YAMLScalar>(element, range) {
    override fun resolve(): PsiElement? {
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

    override fun getVariants(): Array<Any> {
        // 返回所有可能的引用名称作为补全候选项
        val project = element.project
        val yamlFiles = findYamlFiles(project)
        val names = mutableListOf<String>()
        for (file in yamlFiles) {
            val psiFile = PsiManager.getInstance(project).findFile(file) ?: continue
            val keyValues = findAllKeyValues(psiFile)
            for (keyValue in keyValues) {
                names.add(keyValue.keyText)
            }
        }
        return names.toTypedArray()
    }


//    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
//        // 返回一个或多个解析结果，如果有多个匹配的键值对，那么让用户选择一个
//        val project = element.project
//        val yamlFiles = findYamlFiles(project)
//        val results = mutableListOf<ResolveResult>()
//        for (file in yamlFiles) {
//            val psiFile = PsiManager.getInstance(project).findFile(file) ?: continue
//            val keyValue = findKeyValueByName(psiFile, name) ?: continue
//            results.add(PsiElementResolveResult(keyValue))
//        }
//        return results.toTypedArray()
//    }

    // 查找项目中的所有yaml文件
    private fun findYamlFiles(project: Project): List<VirtualFile> {
        println("ssss")
        val root = ProjectRootManager.getInstance(project).contentRoots.firstOrNull() ?: return emptyList()
        println(root)
//        root = "D:\\project\\oldme-api\\manifest\\config"
        val files = mutableListOf<VirtualFile>()
//        root.refresh(false, true)
//        root.it
//        root.iterateContent {
//            if (it.extension == "yaml") {
//                files.add(it)
//            }
//            true
//        }
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
