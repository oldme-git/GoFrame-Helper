package com.github.oldmegit.goframehelper.callUtil.orm

import com.github.oldmegit.goframehelper.callUtil.CallUtil
import com.github.oldmegit.goframehelper.callUtil.cfg.CfgUtil
import com.goide.psi.*
import com.goide.psi.impl.GoPsiUtil
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ObjectUtils

object OrmUtil : CallUtil {
    override fun getData(psiElement: PsiElement): Map<String, PsiElement?> {
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
        var ctx = ""
        if (psiElement == null) {
            return ctx
        }
        val psiComment = psiElement.nextSibling.nextSibling
        ctx = extractTextFromComment(psiComment.text)
        return ctx
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
    private fun getTableData(columnType: GoType): Map<String, PsiElement?> {
        val typeSpec = columnType.resolve(ResolveState.initial()) as GoTypeSpec
        val fields = PsiTreeUtil.findChildrenOfType(typeSpec, GoFieldDeclaration::class.java)
        val data: MutableMap<String, PsiElement?> = hashMapOf()
        for (field in fields) {
            val fieldDefinition = field.firstChild
            if (fieldDefinition !is GoFieldDefinition) {
                continue
            }
            val psiComment = field.nextSibling.nextSibling
            var comment : PsiElement? = null
            if (psiComment is PsiComment) {
                comment = psiComment
            }
            data[fieldDefinition.text.camelToSnakeCase()] = comment
        }
        return data
    }

    // handle type GoAssignmentStatement
    // for example:
    // db = db.Xxx
    private fun doAssignmentStatement(assignmentStatement: GoAssignmentStatement): GoStatement? {
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

        // db.Xxx
        val list = PsiTreeUtil.findChildrenOfType(simpleStatement, GoReferenceExpression::class.java)
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

    // camel to snake case
    private fun String.camelToSnakeCase(): String {
        val regex = "(?<=[a-zA-Z])[A-Z]".toRegex()
        return regex.replace(this) { "_${it.value}" }.lowercase()
    }
}