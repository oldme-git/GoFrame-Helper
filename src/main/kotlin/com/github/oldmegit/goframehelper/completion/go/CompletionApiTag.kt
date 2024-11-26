package com.github.oldmegit.goframehelper.completion.go

import com.github.oldmegit.goframehelper.data.apiTagValueUtil.*
import com.github.oldmegit.goframehelper.data.apiTagValueUtil.TagValue
import com.github.oldmegit.goframehelper.gf.Gf
import com.goide.psi.GoAnonymousFieldDefinition
import com.goide.psi.GoFieldDefinition
import com.goide.psi.GoStructType
import com.intellij.codeInsight.completion.CompletionUtilCore
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement

class CompletionApiTag: CompletionBase() {
    private val valueObject = mapOf<String, TagValue>(
        "method" to Method,
        "v" to Validation,
        "mime" to Mime,
        "in" to In
    )

    override fun addCompletionsEvent() {
        // only struct name containing "Req" or "Res" can need code completion
        if (!isValidStruct()) {
            return
        }

        if (positionIsTagKey()) {
            keyCodeCompletion()
        } else {
            valueCodeCompletion()
        }
    }

    override fun isValidFolder(): Boolean {
        return true
    }

    private fun getStructType(): PsiElement? {
        val structType = position.parent.parent.parent.parent
        if (structType is GoStructType) {
            return structType
        }
        return null
    }

    // Check struct name is end with "Req" or "Res"
    // GoFrame have removed the requirement for struct name to end with "Req" or "Res"
    // https://github.com/gogf/gf/commit/3d4904eb3dc558aebca89146f846d9852f3e0694
    private fun isValidStruct(): Boolean {
        return true
        val structType = getStructType()
        if (structType == null) {
            return false
        }

        val structName = structType.prevSibling.prevSibling.text
        val lastStr = structName.takeLast(3)
        return lastStr == "Req" || lastStr == "Res"
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
            m.putAll(Gf.openApiTagGMeta)
        } else {
            m.putAll(Gf.openApiTagNormal)
        }

        return m
    }

    // check position is tag key
    private fun positionIsTagKey(): Boolean {
        val rawText = position.text
        val tagValuePattern = String.format("\\w+:\"[^\"]*?%s.*?\"", CompletionUtilCore.DUMMY_IDENTIFIER_TRIMMED)
        val regex = Regex(tagValuePattern)
        return !rawText.contains(regex)
    }

    // tag key code completion
    private fun keyCodeCompletion() {
        val text = position.text.replace("`", "")
        val textArr = text.split(CompletionUtilCore.DUMMY_IDENTIFIER)
        var prefix = textArr[0]

        if (prefix.endsWith(" ")) {
            prefix = ""
        }

        val lastSpaceIndex = prefix.lastIndexOf(" ")
        val lastPrefixOfTag = prefix.substring(lastSpaceIndex + 1)
        val tagList = getFieldNameTag()

        for ((k, v) in tagList) {
            if (k.startsWith(lastPrefixOfTag)) {
                keyAddElement(prefix + k.removePrefix(lastPrefixOfTag), k , v)
            }
        }
    }

    // add tag key element
    private fun keyAddElement(lookupString: String, text: String, tail: String = "") {
        result.addElement(
            LookupElementBuilder.create(lookupString)
            .withInsertHandler { ctx, _ ->
                ctx.document.insertString(ctx.tailOffset, ":\"\"")
                ctx.editor.caretModel.moveToOffset(ctx.editor.caretModel.offset + 2)
            }
            .withIcon(Gf.icon)
            .withTailText(" $tail", true)
            .withPresentableText(text)
        )
    }

    private fun valueCodeCompletion() {
        val text = position.text.replace("`", "")
        val textArr = text.split(CompletionUtilCore.DUMMY_IDENTIFIER)
        val prefix = textArr[0]

        val patten = "\\w+(?=:\")"
        val regex = patten.toRegex()
        val keyList = regex.findAll(prefix)
        val current = keyList.last().value
        val tagValue = valueObject[current] ?: return
        val list = tagValue.list
        val separator = tagValue.separator
        var valueStartIndex = -1

        if (separator != "") {
            valueStartIndex = prefix.lastIndexOf(separator)
        }
        if (valueStartIndex == -1) {
            valueStartIndex = prefix.lastIndexOf("\"")
        }

        val valuePrefixOfTag = prefix.substring(valueStartIndex + 1).lowercase()

        for ((k, v) in list) {
            if (k.startsWith(valuePrefixOfTag)) {
                valueAddElement(prefix + k.removePrefix(valuePrefixOfTag), k, v)
            }
        }
    }

    // add tag value element
    private fun valueAddElement(lookupString: String, text: String, tail: String = "") {
        result.addElement(
            LookupElementBuilder.create(lookupString)
                .withIcon(Gf.icon)
                .withTailText(" $tail", true)
                .withPresentableText(text)
        )
    }
}