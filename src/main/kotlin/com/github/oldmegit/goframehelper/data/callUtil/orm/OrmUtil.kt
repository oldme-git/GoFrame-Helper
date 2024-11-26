package com.github.oldmegit.goframehelper.data.callUtil.orm

import com.github.oldmegit.goframehelper.data.callUtil.CallUtil
import com.goide.psi.*
import com.goide.psi.impl.GoElementImpl
import com.goide.psi.impl.GoKeyImpl
import com.goide.psi.impl.GoPsiUtil
import com.goide.psi.impl.GoValueImpl
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ObjectUtils

object OrmUtil : CallUtil() {
    override fun getData(psiElement: PsiElement): Map<String, Set<PsiElement>> {
        return try {
            val statement = getStatementContainDao(psiElement)
            val dao = getDaoByStatement(statement!!)
            val column = getColumnByDao(dao!!)
            getTableData(column as GoType)
        } catch (_: Exception) {
            hashMapOf()
        }
    }

    override fun getPsiTail(psiElement: PsiElement?): String {
        var text = ""
        if (psiElement == null) {
            return text
        }

        val psiComment = psiElement.nextSibling.nextSibling
        if (psiComment !is PsiComment) {
            return text
        }
        text = extractTextFromComment(psiComment.text)
        return text
    }

    // get statement contain XXXDao by given PsiElement
    private fun getStatementContainDao(psiElement: PsiElement): GoStatement? {
        // get direct statement
        var statement = PsiTreeUtil.findFirstParent(psiElement) { e: PsiElement? ->
            e is GoStatement
        } as GoStatement?

        if (statement is GoAssignmentStatement) {
            statement = doAssignmentStatement(statement)
        } else if (statement is GoSimpleStatement) {
            statement = doSimpleStatement(statement)
        }

        return statement
    }

    // get Xxx dao by statement
    private fun getDaoByStatement(statement: GoStatement): GoStructType? {
        for (callExpr in PsiTreeUtil.findChildrenOfType(statement, GoCallExpr::class.java)) {
            val reference = GoPsiUtil.getCallReference(callExpr)
            // get the declaration of fun
            val declaration = ObjectUtils.tryCast(
                reference!!.resolve(),
                GoMethodDeclaration::class.java
            )

            // get the return type of fun's declaration
            var resultType = declaration!!.resultType

            // get the gotype if return type is gotype pointer
            // resultType is Model or gdb.Model
            if (resultType is GoPointerType) {
                resultType = resultType.type!!
            }

            if (resultType.resolve(ResolveState.initial()) !is GoTypeSpec) {
                continue
            }

            val pointerType = declaration.receiver!!.type
            if (pointerType !is GoPointerType) {
                continue
            }
            val type = pointerType.type ?: return null
            if (!type.text?.endsWith("Dao")!!) {
                continue
            }

            val spec = type.resolve(ResolveState.initial())
            if (spec is GoTypeSpec) {
                return spec.specType.type as GoStructType
            }
        }
        return null
    }

    // get column GoType by XxxDao of type GoStructType
    private fun getColumnByDao(structType: GoStructType): GoType? {
        for (field in structType.fieldDeclarationList) {
            val name = field.fieldDefinitionList[0].name?.lowercase()
            if (name?.endsWith("columns")!!) {
                return field.type
            }
        }
        return null
    }

    // get table data by XXXColumns of type GoTypeSpec
    private fun getTableData(columnType: GoType): Map<String, Set<PsiElement>> {
        val goTypeDeclaration = PsiTreeUtil.findFirstParent(columnType) { e: PsiElement? ->
            e is GoTypeDeclaration
        } as GoTypeDeclaration
        val goVarDefinition = findGoVarDefinition(goTypeDeclaration)
        val varFields = PsiTreeUtil.findChildrenOfType(goVarDefinition, GoElementImpl::class.java)
        val varData = mutableMapOf<String, String>()
        for (field in varFields) {
            val fieldKey = field.firstChild
            if (fieldKey !is GoKeyImpl) {
                continue
            }
            if (field != null) {
                val k = PsiTreeUtil.findChildrenOfType(field, GoKeyImpl::class.java).first()
                val v = PsiTreeUtil.findChildrenOfType(field, GoValueImpl::class.java).first()
                val vText = v.text.removeQuotes()
                varData[k.text] = vText
            }
        }

        val typeSpec = columnType.resolve(ResolveState.initial()) as GoTypeSpec
        val typeFields = PsiTreeUtil.findChildrenOfType(typeSpec, GoFieldDeclaration::class.java)
        val typeData = mutableMapOf<String, Set<PsiElement>>()

        for (field in typeFields) {
            val fieldDefinition = field.firstChild
            if (fieldDefinition !is GoFieldDefinition) {
                continue
            }
            if (field != null) {
                typeData[fieldDefinition.text] = setOf(field)
            } else {
                typeData[fieldDefinition.text] = hashSetOf()
            }
        }

        val data = mutableMapOf<String, Set<PsiElement>>()
        for ((k, v) in varData) {
            data[v] = typeData[k]!!
        }

        return data
    }

    // find GoVarDefinition by PsiElement
    // for example:
    // var linkColumns = LinkColumns{
    //	Id:          "id",
    //	Name:        "name",
    //}
    private fun findGoVarDefinition(psiElement: PsiElement?): GoVarDeclaration? {
        val current = psiElement?.nextSibling
        return if (current is GoVarDeclaration) {
            current
        } else {
            findGoVarDefinition(current)
        }
    }

    // handle type GoAssignmentStatement
    // for example:
    // db = db.Xxx
    // db = dao.Xxx
    private fun doAssignmentStatement(assignmentStatement: GoAssignmentStatement): GoStatement? {
        // get GoReferenceExpression list
        for (one in assignmentStatement.expressionList) {
            // dao.Xxx
            // if statement contain dao
            if (one.text.startsWith("dao.")) {
                return assignmentStatement
            }
        }
        for (expression in assignmentStatement.leftHandExprList.expressionList) {
            val psiElementRoot = expression.reference?.resolve()
            return getStatementContainDao(psiElementRoot!!)
        }
        return null
    }

    // handle type GoSimpleStatement
    // for example:
    // db := db.Xxx
    // db.Xxx or dao.Xxx
    private fun doSimpleStatement(simpleStatement: GoSimpleStatement): GoStatement? {
        val declaration = simpleStatement.shortVarDeclaration

        // direct return if db := db.Xxx
        if (declaration != null) {
            return simpleStatement
        }

        // get GoReferenceExpression list
        val list = PsiTreeUtil.findChildrenOfType(simpleStatement, GoReferenceExpression::class.java)
        for (one in list) {
            // dao.Xxx
            // if statement contain dao
            if (one.text == "dao") {
                return simpleStatement
            }
        }

        // db.Xxx
        val last = list.last()
        if (last.resolve() is GoVarDefinition) {
            return getStatementContainDao(last.resolve() as GoVarDefinition)
        }

        return simpleStatement
    }

    private fun extractTextFromComment(comment: String): String {
        if (!comment.startsWith("//")) {
            return comment
        }
        if (comment == "//") {
            return ""
        }
        return comment.drop(2)
    }

    // remove first and last double quotes
    private fun String?.removeQuotes(): String {
        return this?.removePrefix("\"")?.removeSuffix("\"") ?: ""
    }
}