package com.github.khodand.pycharmplugin.inspection.quickFixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiComment
import com.intellij.util.IncorrectOperationException
import com.jetbrains.python.psi.PyAssignmentStatement
import com.jetbrains.python.psi.PyElementGenerator
import com.jetbrains.python.psi.LanguageLevel

/**
 *
 */
class AssignmentAnnotationQuickFix : LocalQuickFix {

    companion object {
        const val QUICK_FIX_NAME = "Declare return type with annotation"
        private val LOG = Logger.getInstance("#com.intellij.codeInspection.ComparingReferencesInspection")
    }

    /**
     *
     */
    override fun getName(): String {
        return QUICK_FIX_NAME
    }

    /**
     *
     */
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        try {
            if (descriptor.psiElement !is PyAssignmentStatement) {
                return
            }

            val statement = descriptor.psiElement as PyAssignmentStatement

            val annotatedAssignment = PyElementGenerator.getInstance(project)
                    .createFromText(LanguageLevel.PYTHON38, PyAssignmentStatement::class.java, "a: int = b")
            statement.leftHandSideExpression?.let { annotatedAssignment.leftHandSideExpression!!.replace(it) }
            statement.assignedValue?.let { annotatedAssignment.assignedValue!!.replace(it) }
            if (statement.lastChild is PsiComment) {
                annotatedAssignment.addAfter(statement.lastChild, annotatedAssignment.lastChild)
            }

            statement.replace(annotatedAssignment)
        } catch (e: IncorrectOperationException) {
            LOG.error(e)
        }
    }

    override fun getFamilyName(): String {
        return name
    }
}
