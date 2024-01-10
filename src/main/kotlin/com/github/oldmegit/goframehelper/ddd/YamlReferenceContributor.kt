package com.github.oldmegit.goframehelper.ddd

// YamlReferenceContributor.kt
import com.goide.GoTypes
import com.goide.psi.GoStringLiteral
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.psi.YAMLScalar


class YamlReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        // 注册一个引用提供者，匹配yaml文件中的字符串字面量
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(GoStringLiteral::class.java),
            YamlReferenceProvider()
        )
    }
}
