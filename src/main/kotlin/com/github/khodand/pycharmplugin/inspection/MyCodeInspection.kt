package com.github.khodand.pycharmplugin.inspection

import com.intellij.codeInspection.*
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.DocumentAdapter
import com.intellij.util.IncorrectOperationException
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.NotNull
import org.jetbrains.rpc.LOG
import java.awt.FlowLayout
import java.util.*
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent

/**
 * Implements an inspection to detect when object references are compared using 'a==b' or 'a!=b'
 * The quick fix converts these comparisons to 'a.equals(b) or '!a.equals(b)' respectively.
 */
public class MyCodeInspection : LocalInspectionTool() {
    val QUICK_FIX_NAME = "SDK: "
    private val LOG = Logger.getInstance("#com.intellij.codeInspection.ComparingReferencesInspection")
    private val myQuickFix = AddStubTypeFix()
    @NonNls
    var CHECKED_CLASSES = "java.lang.String;java.util.Date"

    override fun createOptionsPanel(): JComponent? {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT))
        val checkedClasses = JTextField(CHECKED_CLASSES)
        checkedClasses.document.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(event: DocumentEvent) {
                CHECKED_CLASSES = checkedClasses.text
            }
        })
        panel.add(checkedClasses)
        return panel
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            @NonNls
            private val DESCRIPTION_TEMPLATE = "SDK " +
                    InspectionsBundle.message("inspection.comparing.references.problem.descriptor")

            fun visitBinaryExpression(expression: PsiBinaryExpression) {
                super.visitBinaryExpression(expression)
                val opSign: IElementType = expression.getOperationTokenType()
                if (opSign === JavaTokenType.EQEQ || opSign === JavaTokenType.NE) {
                    // The binary expression is the correct type for this inspection
                    val lOperand: PsiExpression = expression.getLOperand()
                    val rOperand: PsiExpression = expression.getROperand()
                    if (rOperand == null || isNullLiteral(lOperand) || isNullLiteral(rOperand)) {
                        return
                    }
                    // Nothing is compared to null, now check the types being compared
                    val lType: PsiType = lOperand.getType()
                    val rType: PsiType = rOperand.getType()
                    if (isCheckedType(lType) || isCheckedType(rType)) {
                        // Identified an expression with potential problems, add to list with fix object.
                        holder.registerProblem(expression,
                                DESCRIPTION_TEMPLATE, myQuickFix)
                    }
                }
            }

            private fun isCheckedType(type: PsiType): Boolean {
                if (type !is PsiClassType) {
                    return false
                }
                val tokenizer = StringTokenizer(CHECKED_CLASSES, ";")
                while (tokenizer.hasMoreTokens()) {
                    val className = tokenizer.nextToken()
                    if (type.equalsToText(className)) {
                        return true
                    }
                }
                return false
            }
        }
    }


    private class AddStubTypeFix:LocalQuickFix {

        override fun getName(): String {
            return QUICK_FIX_NAME
        }

        override fun getFamilyName(): String {
            return name
        }

        public override fun applyFix(@NotNull project: Project, @NotNull descriptor: ProblemDescriptor) {
            try {
                val psiElement = descriptor.psiElement
                var leaf = PsiTreeUtil.nextLeaf(psiElement)
                while (leaf != null)
                {
//                    PsiSymbolDeclaration
//                    PsiDeclaredTarget
//                    PsiNamedElement
//                    PsiLiteralValue
                    //leaf.navigationElement
                    if (leaf is PsiElement) {
                        println(leaf.text)
                    }
                    leaf = PsiTreeUtil.nextLeaf(leaf)
                }
//                val binaryExpression: PsiBinaryExpression = descriptor.psiElement as PsiBinaryExpression
//                val opSign: IElementType = binaryExpression.getOperationTokenType()
//                val lExpr: PsiExpression = binaryExpression.getLOperand()
//                val rExpr: PsiExpression = binaryExpression.getROperand() ?: return
//                val factory: PsiElementFactory = JavaPsiFacade.getInstance(project).getElementFactory()
//                val equalsCall: PsiMethodCallExpression = factory.createExpressionFromText("a.equals(b)", null) as PsiMethodCallExpression
//                equalsCall.getMethodExpression().getQualifierExpression().replace(lExpr)
//                equalsCall.getArgumentList().getExpressions().get(0).replace(rExpr)
//                val result: PsiExpression = binaryExpression.replace(equalsCall) as PsiExpression
//                if (opSign === JavaTokenType.NE) {
//                    val negation: PsiPrefixExpression = factory.createExpressionFromText("!a", null) as PsiPrefixExpression
//                    negation.getOperand().replace(result)
//                    result.replace(negation)
//                }
            } catch (e: IncorrectOperationException) {
                LOG.error(e)
            }
 //           TODO("Not yet implemented")
        }
    }
}