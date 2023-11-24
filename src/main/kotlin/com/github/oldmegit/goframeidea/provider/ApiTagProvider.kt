package com.github.oldmegit.goframeidea.provider

import com.goide.psi.GoAnonymousFieldDefinition
import com.goide.psi.GoFieldDefinition
import com.goide.psi.GoStructType
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import java.io.File
import java.nio.file.Paths

class ApiTagProvider: CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val position = parameters.position
        val project = position.project
        val basePath = project.basePath
        val filePath = parameters.originalFile.virtualFile.path

        // check if the file is in the api dir
        if (!isApiFile(basePath.toString(), filePath)) {
            return
        }

        // only struct name containing "Req" can need code completion
        if (!isStructReq(position)) {
            return
        }

        // get filed name
        val filedName = getFieldName(position)
        println(filedName)

        result.addElement(LookupElementBuilder.create("hello1"))
        result.addElement(LookupElementBuilder.create("hello2"))
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

    private fun isStructReq(position: PsiElement): Boolean {
        val structType = getStructType(position)
        if (structType == null) {
            return false
        }

        val structName = structType.prevSibling.prevSibling.text
        return structName.takeLast(3) == "Req"
    }

    private fun getFieldName(position: PsiElement): String {
        val name = getFiledDefinition(position)?.text
        if (name != null) {
            return name
        }
        return ""
    }

    // check if the file is in the api dir
    private fun isApiFile(base: String, file: String): Boolean {
        val basePath = Paths.get(base)
        val filePath = Paths.get(file)
        var absolute = basePath.relativize(filePath).toString()
        if (File.separator == "\\") {
            absolute = absolute.replace("\\", "/")
        }
        val absolutePaths = absolute.split("/")
        return absolutePaths[0] == "api"
    }
}