package com.github.oldmegit.goframehelper.ddd

// YamlReferenceProvider.kt
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.psi.YAMLScalar

class YamlReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        // 如果元素是一个字符串字面量，且包含方括号，那么创建一个引用
        if (element is YAMLScalar) {
            val text = element.textValue
            val match = Regex("""\[(\w+)]""").find(text)
            if (match != null) {
                val range = TextRange(match.range.first, match.range.last + 1)
                val name = match.groupValues[1]
                return arrayOf(YamlReference(element, range, name))
            }
        }
        return PsiReference.EMPTY_ARRAY
    }
}
