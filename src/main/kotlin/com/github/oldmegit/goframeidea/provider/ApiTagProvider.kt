package com.github.oldmegit.goframeidea.provider

import com.github.oldmegit.goframeidea.gf.Gf
import com.github.oldmegit.goframeidea.ui.AppSettingsState
import com.goide.psi.GoAnonymousFieldDefinition
import com.goide.psi.GoFieldDefinition
import com.goide.psi.GoStructType
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import java.io.File
import java.nio.file.Paths

class ApiTagProvider: GfProvider() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val position = parameters.position
        val project = position.project
        val basePath = project.basePath
        val filePath = parameters.originalFile.virtualFile.path

        if (!super.isGf(position)) {
            return
        }

        // check if the file is in the api dir
        if (!isLegalFolder(basePath.toString(), filePath)) {
            return
        }

        // only struct name containing "Req" or "Res" can need code completion
        if (!isLegalStruct(position)) {
            return
        }

        // get filed name
        val filedName = getFieldName(position)

        if (filedName == "g.Meta") {
            for ((text, tailText) in Gf.openApiTagGMeta) {
                codeCompletionSet(result, text, tailText)
            }
        }

        for ((text, tailText) in Gf.openApiTag) {
            codeCompletionSet(result, text, tailText)
        }
    }

    private fun getStructType(position: PsiElement): PsiElement? {
        val structType = position.parent.parent.parent.parent
        if (structType is GoStructType) {
            return structType
        }
        return null
    }

    private fun getFiledDefinition(position: PsiElement): PsiElement? {
        val fieldDefinition = position.parent.parent.parent.firstChild
        if (fieldDefinition is GoFieldDefinition || fieldDefinition is GoAnonymousFieldDefinition) {
            return fieldDefinition
        }
        return null
    }

    private fun isLegalStruct(position: PsiElement): Boolean {
        val structType = getStructType(position)
        if (structType == null) {
            return false
        }

        val structName = structType.prevSibling.prevSibling.text
        val lastStr = structName.takeLast(3)
        return lastStr == "Req" || lastStr == "Res"
    }

    private fun getFieldName(position: PsiElement): String {
        val name = getFiledDefinition(position)?.text
        if (name != null) {
            return name
        }
        return ""
    }

    override fun isLegalFolder(folderPath: String, filePath: String): Boolean {
        val basePathCls = Paths.get(folderPath)
        val filePathCls = Paths.get(filePath)
        var absolute = basePathCls.relativize(filePathCls).toString()
        if (File.separator == "\\") {
            absolute = absolute.replace("\\", "/")
        }
        val absolutePaths = absolute.split("/")
        val settings = AppSettingsState.getInstance()
        return absolutePaths[0] == settings.gfApiDir
    }

    private fun codeCompletionSet(result: CompletionResultSet, text: String, tailText: String) {
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
}