package com.github.khodand.pycharmplugin.inspection

import com.github.khodand.pycharmplugin.inspection.quickFixes.AssignmentAnnotationQuickFix
import com.intellij.codeInspection.*
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.util.IncorrectOperationException
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.psi.PyAssignmentStatement
import com.jetbrains.python.psi.PyElementVisitor
import org.jetbrains.annotations.NonNls


class CodeInspection : PyInspection() {
    private val assignmentQuickFix = AssignmentAnnotationQuickFix()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PyElementVisitor {
        return object : PyElementVisitor() {
            @NonNls
            private val DESCRIPTION_TEMPLATE = "SDK adsfafrea"

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
                            DESCRIPTION_TEMPLATE, assignmentQuickFix)
                }
            }
        }
    }


    private class FunctionAnnotationQuickFix : LocalQuickFix {
        override fun getName(): String {
            return QUICK_FIX_NAME
        }

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            try {
                if (descriptor.psiElement !is PyAssignmentStatement)
                    return

                val statement = descriptor.psiElement as PyAssignmentStatement
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