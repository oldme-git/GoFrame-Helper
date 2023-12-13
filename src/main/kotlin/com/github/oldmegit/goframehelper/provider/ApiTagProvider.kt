package com.github.oldmegit.goframehelper.provider

import com.github.oldmegit.goframehelper.gf.Gf
import com.goide.psi.GoAnonymousFieldDefinition
import com.goide.psi.GoFieldDefinition
import com.goide.psi.GoStructType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

class ApiTagProvider: GfProvider() {
    override fun addCompletionsEvent() {
        // only struct name containing "Req" or "Res" can need code completion
        if (!isValidStruct()) {
            return
        }

        if (positionIsTagKey()) {
            codeCompletionTagKey()
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

    private fun codeCompletionTagKey(text: String, tailText: String) {
        result.addElement(
            LookupElementBuilder.create(text)
                .withInsertHandler { ctx, _ ->
                    ctx.document.insertString(ctx.tailOffset, ":\"\"")
                    ctx.editor.caretModel.moveToOffset(ctx.editor.caretModel.offset + 2)
                }
                .withIcon(Gf.icon)
                .withTailText(" $tailText", true)
        )
    }

    // check position is tag key
    private fun positionIsTagKey(): Boolean {
        val rawText = position.text
        if (!rawText.contains("\"")) {
            return true
        }
        val offset = parameters.offset
        var textRange = TextRange(offset - 2, offset)
        var text = parameters.editor.document.getText(textRange)
        if (text != "\" ") {
            return false
        }
        textRange = TextRange(offset - 3, offset)
        text = parameters.editor.document.getText(textRange)
        return text != ":\" "
    }

    private fun codeCompletionTagKey() {
        // get filed name
        val filedName = getFieldName()

        if (filedName == "g.Meta") {
            for ((text, tailText) in Gf.openApiTagGMeta) {
                codeCompletionTagKey(text, tailText)
            }
        } else {
            for ((text, tailText) in Gf.openApiTagNormal) {
                codeCompletionTagKey(text, tailText)
            }
        }

        for ((text, tailText) in Gf.openApiTag) {
            codeCompletionTagKey(text, tailText)
        }
    }

    // check position is tag value
//    private fun positionIsTagValue(tagKey: String): Boolean {
//        val text = position.text
//        val tagKeyMark = "$tagKey:\""
//        val tagStart = text.indexOf(tagKeyMark)
//        val tagEnd = text.indexOf("\"", tagStart + tagKeyMark.length)
//        val offset = parameters.offset
//        val textRange = position.textRange
//        val stringOffset = offset - textRange.startOffset
//
//        val vTextRange = TextRange(tagStart, tagEnd)
//        return textRange.contains(offset) && vTextRange.contains(stringOffset)
//    }
//
//    private fun codeCompletionTagValue(text: String, tailText: String) {
//        result.addElement(
//            LookupElementBuilder.create(text)
//                .withIcon(Gf.icon)
//                .withTailText(" $tailText", true)
//        )
//    }

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
}