package com.github.oldmegit.goframeidea.provider

import com.github.oldmegit.goframeidea.gf.Gf
import com.github.oldmegit.goframeidea.ui.AppSettingsState
import com.goide.psi.GoAnonymousFieldDefinition
import com.goide.psi.GoFieldDefinition
import com.goide.psi.GoStructType
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import java.io.File
import java.nio.file.Paths

class ApiTagProvider: GfProvider() {
    private lateinit var parameters: CompletionParameters
    private lateinit var position: PsiElement
    private lateinit var context: ProcessingContext
    private lateinit var result: CompletionResultSet

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        this.parameters = parameters
        this.position = parameters.position
        this.context = context
        this.result = result

        if (!super.isGf(position)) {
            return
        }

        // check if the file is in the api dir
        // only struct name containing "Req" or "Res" can need code completion
        if (!(isLegalFolder() && isLegalStruct())) {
            return
        }


        if (positionIsTagKey()) {
            codeCompletionTagKey()
        }
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

    // check position is tag value
    private fun positionIsTagValue(tagKey: String): Boolean {
        val text = position.text
        val tagKeyMark = "$tagKey:\""
        val tagStart = text.indexOf(tagKeyMark)
        val tagEnd = text.indexOf("\"", tagStart + tagKeyMark.length)
        val offset = parameters.offset
        val textRange = position.textRange
        val stringOffset = offset - textRange.startOffset

        val vTextRange = TextRange(tagStart, tagEnd)
        return textRange.contains(offset) && vTextRange.contains(stringOffset)
    }

    private fun codeCompletionTagKey() {
        // get filed name
        val filedName = getFieldName()

        if (filedName == "g.Meta") {
            for ((text, tailText) in Gf.openApiTagGMeta) {
                codeCompletionTagKey(text, tailText)
            }
        } else {
            for ((text, tailText) in Gf.openApiTagNorMal) {
                codeCompletionTagKey(text, tailText)
            }
        }

        for ((text, tailText) in Gf.openApiTag) {
            codeCompletionTagKey(text, tailText)
        }
    }

    private fun getStructType(): PsiElement? {
        val structType = position.parent.parent.parent.parent
        if (structType is GoStructType) {
            return structType
        }
        return null
    }

    private fun getFiledDefinition(): PsiElement? {
        val fieldDefinition = position.parent.parent.parent.firstChild
        if (fieldDefinition is GoFieldDefinition || fieldDefinition is GoAnonymousFieldDefinition) {
            return fieldDefinition
        }
        return null
    }

    private fun isLegalStruct(): Boolean {
        val structType = getStructType()
        if (structType == null) {
            return false
        }

        val structName = structType.prevSibling.prevSibling.text
        val lastStr = structName.takeLast(3)
        return lastStr == "Req" || lastStr == "Res"
    }

    private fun getFieldName(): String {
        val name = getFiledDefinition()?.text
        if (name != null) {
            return name
        }
        return ""
    }

    override fun isLegalFolder(): Boolean {
        val project = position.project
        val basePathCls = Paths.get(project.basePath.toString())
        val filePathCls = Paths.get(parameters.originalFile.virtualFile.path)
        var absolute = basePathCls.relativize(filePathCls).toString()
        if (File.separator == "\\") {
            absolute = absolute.replace("\\", "/")
        }
        val absolutePaths = absolute.split("/")
        val settings = AppSettingsState.getInstance()
        return absolutePaths[0] == settings.gfApiDir
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

    private fun codeCompletionTagValue(text: String, tailText: String) {
        result.addElement(
            LookupElementBuilder.create(text)
                .withIcon(Gf.icon)
                .withTailText(" $tailText", true)
        )
    }
}