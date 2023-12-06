package com.github.oldmegit.goframeidea.provider

import com.goide.psi.*
import com.goide.psi.impl.GoPsiUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ObjectUtils


class OrmProvider : GfProvider() {
    override fun addCompletionsEvent() {
//        println(1)

        val call = PsiTreeUtil.findFirstParent(this.position) { e: PsiElement? ->
            e is GoCallExpr
        } as GoCallExpr? ?: return

//        val descriptor = OrmCallables.find(call, false)
//        println(descriptor == null)
        getTable()
    }

    private fun getBaseState(psiElement: PsiElement): GoStatement? {
        // get direct statement
        val positionStatement = PsiTreeUtil.findFirstParent(psiElement) { e: PsiElement? ->
            e is GoStatement
        } as GoStatement?

        if (positionStatement is GoAssignmentStatement) {
            // if direct statement is kind of assign
            // for example:
            // db = db.Xxx
            for (expression in positionStatement.leftHandExprList.expressionList) {
                val psiElementRoot = expression.reference?.resolve()
                return getBaseState(psiElementRoot!!)
            }
        } else if (positionStatement is GoSimpleStatement) {
            // if direct statement is kind of simple
            // for example:
            // db := db.Xxx
            // db.Xxx or dao.Xxx
            val declaration = positionStatement.shortVarDeclaration
            // direct return if db := db.Xxx
            if (declaration != null) {
                // TODO 待优化
                return positionStatement
            }

            // db.Xxx
            val list = PsiTreeUtil.findChildrenOfType(positionStatement, GoReferenceExpression::class.java)
            val last = list.last()
            if (last.resolve() is GoVarDefinition) {
                return getBaseState(last.resolve() as GoVarDefinition)
            }

            // dao.Xxx
            // TODO 待优化
        }

        return positionStatement
    }

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

            // get the type if return type is pointer
            if (resultType is GoPointerType) {
                resultType = resultType.type!!
            }

            if (resultType.resolve(ResolveState.initial()) is GoTypeSpec) {
                if (declaration.receiver!!.type is GoPointerType) {
                    val pointerType = declaration.receiver!!.type as GoPointerType
                    val type = pointerType.type ?: return null
                    if (type.text?.endsWith("Dao")!!) {
                        val spec = type.resolve(ResolveState.initial())
                        if (spec is GoTypeSpec) {
                            return spec.specType.type as GoStructType
                        }
                    }
                }
            }
        }
        return null
    }

    private fun getColumn(structType: GoStructType): GoType? {
        for (field in structType.fieldDeclarationList) {
            val name = field.fieldDefinitionList[0].name?.lowercase()
            if (name?.endsWith("columns")!!) {
                return field.type
            }
        }
        return null
    }

    fun getTable() {
        val statement = getBaseState(position)

        val dao = getDaoByStatement(statement!!)

        val column = getColumn(dao!!)

        println(column?.text)

//        val goType = column as GoType
//        val goTypeSpec = goType.resolve(ResolveState.initial()) as GoTypeSpec
//        for (search in GoReferencesSearch.search(goTypeSpec)) {
//            val goCompositeLit = search.getElement().getParent() as GoCompositeLit
//            val goLiteralValue = goCompositeLit.getLiteralValue()
//            if (goLiteralValue != null) {
//                for (goElement in goLiteralValue.elementList) {
//                    if (goElement.key != null && goElement.key!!.fieldName != null && goElement.value != null && goElement.value!!
//                            .expression is GoStringLiteral
//                    ) {
//                        println(goElement.key!!.fieldName!!.identifier.text)
//                        val goStringLiteral = goElement.getValue()?.getExpression() as GoStringLiteral
//                        println(goStringLiteral.getDecodedText())
//                    }
//                }
//            }
//        }
    }

    override fun isValidFolder(): Boolean {
        return true
    }

    fun getCallName(call: GoCallExpr): String {
        val callRef = GoPsiUtil.getCallReference(call)
        return callRef?.identifier?.text.toString()
    }
}