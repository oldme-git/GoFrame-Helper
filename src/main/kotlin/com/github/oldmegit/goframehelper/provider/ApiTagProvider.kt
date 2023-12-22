package com.github.oldmegit.goframehelper.provider

import com.github.oldmegit.goframehelper.gf.Gf
import com.goide.psi.GoAnonymousFieldDefinition
import com.goide.psi.GoFieldDefinition
import com.goide.psi.GoStructType
import com.goide.psi.GoTag
import com.intellij.codeInsight.completion.CompletionUtilCore
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset

class ApiTagProvider: GfProvider() {
    private val tagValuePattern = String.format("\\w+:\"[^\"]*?%s.*?\"", CompletionUtilCore.DUMMY_IDENTIFIER_TRIMMED)

    override fun addCompletionsEvent() {
        // only struct name containing "Req" or "Res" can need code completion
        if (!isValidStruct()) {
            return
        }

        println("----")

        var text = position.text
        text = text.replace("`", "")
        val textArr = text.split(CompletionUtilCore.DUMMY_IDENTIFIER)
        val firstText = textArr[0]
        var secondText = textArr[1]

        if (!positionIsTagKey()) {
            return
        }

        val prefix: String
        if (firstText.last().toString() == " ") {
            prefix = ""
        } else {
            prefix = firstText.dropLast(1)
        }
        val tagList = getFieldNameTag()

        for ((k, v) in tagList) {
            println(firstText)
            codeCompletionTagKey(prefix + k, v, k)
        }
    }

    override fun isValidFolder(): Boolean {
        return Gf.isApiFile(position.project, parameters.originalFile.virtualFile)
    }

    private fun isValidStruct(): Boolean {
        val structType = getStructType()
        if (structType == null) {
            return false
        }

        val structName = structType.prevSibling.prevSibling.text
        val lastStr = structName.takeLast(3)
        return lastStr == "Req" || lastStr == "Res"
    }

    private fun codeCompletionTagKey(text: String, tailText: String, tableText: String = "") {
        result.addElement(
            LookupElementBuilder.create(text)
            .withInsertHandler { ctx, _ ->
                ctx.document.insertString(ctx.tailOffset, ":\"\"")
                ctx.editor.caretModel.moveToOffset(ctx.editor.caretModel.offset + 2)
            }
            .withIcon(Gf.icon)
            .withTailText(" $tailText", true)
            .withPresentableText(tableText)
        )
    }

    // check position is tag key
    private fun positionIsTagKey(): Boolean {
        val rawText = position.text
        val regex = Regex(tagValuePattern)
        return !rawText.contains(regex)
    }

    private fun codeCompletionTagValue(text: String, tailText: String) {
        result.addElement(
            LookupElementBuilder.create(text)
                .withIcon(Gf.icon)
                .withTailText(" $tailText", true)
        )
    }

    private fun getStructType(): PsiElement? {
        val structType = position.parent.parent.parent.parent
        if (structType is GoStructType) {
            return structType
        }
        return null
    }

    private fun getFiledPsiElement(): PsiElement? {
        val fieldDefinition = position.parent.parent.parent.firstChild
        if (fieldDefinition is GoFieldDefinition || fieldDefinition is GoAnonymousFieldDefinition) {
            return fieldDefinition
        }
        return null
    }

    private fun getFieldName(): String {
        val name = getFiledPsiElement()?.text
        if (name != null) {
            return name
        }
        return ""
    }

    private fun getFieldNameTag() : Map<String, String> {
        // get filed name
        val filedName = getFieldName()
        val m = hashMapOf<String, String>()
        m.putAll(Gf.openApiTag)

        if (filedName == "g.Meta") {
            m.putAll(Gf.openApiTag)

        } else {
            m.putAll(Gf.openApiTagNormal)
        }

        return m
    }
}