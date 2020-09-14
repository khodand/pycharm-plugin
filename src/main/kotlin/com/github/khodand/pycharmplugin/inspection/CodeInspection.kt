package com.github.khodand.pycharmplugin.inspection

import com.intellij.codeInspection.*
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.ui.DocumentAdapter
import com.intellij.util.IncorrectOperationException
import com.jetbrains.python.psi.PyAssignmentStatement
import org.jetbrains.annotations.NonNls
import java.awt.FlowLayout
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent

/**
 * Implements an inspection to detect when object references are compared using 'a==b' or 'a!=b'
 * The quick fix converts these comparisons to 'a.equals(b) or '!a.equals(b)' respectively.
 */
class CodeInspection : LocalInspectionTool() {
    private val myQuickFix = CriQuickFix()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            @NonNls
            private val DESCRIPTION_TEMPLATE = "SDK adsfafrea"

            //             public void visitElement(@NotNull PsiElement element) {
            //                ProgressIndicatorProvider.checkCanceled();
            //            }
            override fun visitElement(element: PsiElement) {
                super.visitElement(element)
                if (element is PyAssignmentStatement) {
                    println("AssignmentStatement: " + element.getText())
                    println("annotationValue: " + element.annotationValue)
                    if (element.annotationValue != null) {
                        return
                    }
                    println("registerProblem")
                    holder.registerProblem(element,
                            DESCRIPTION_TEMPLATE, myQuickFix)
                }
            }
        }
    }

    private class CriQuickFix : LocalQuickFix {
        override fun getName(): String {
            return QUICK_FIX_NAME
        }

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            try {
                val statement = descriptor.psiElement as PyAssignmentStatement
                if (statement.annotationValue != "") {
                    return
                }
                //PyStubElementType
                //PyAnnotationElementType
                //statement.addAfter(new PyAnnotationImpl(PyStubElementTypes.ANNOTATION), statement.getFirstChild());
            } catch (e: IncorrectOperationException) {
                LOG.error(e)
            }
        }

        override fun getFamilyName(): String {
            return name
        }
    }

    companion object {
        // Defines the text of the quick fix intention
        val QUICK_FIX_NAME = "SDK: Helo boy"
        private val LOG = Logger.getInstance("#com.intellij.codeInspection.ComparingReferencesInspection")
    }
}