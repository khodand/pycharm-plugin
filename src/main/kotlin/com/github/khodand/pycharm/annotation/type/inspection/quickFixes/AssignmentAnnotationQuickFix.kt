package com.github.khodand.pycharm.annotation.type.inspection.quickFixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiComment
import com.intellij.util.IncorrectOperationException
import com.jetbrains.python.psi.LanguageLevel
import com.jetbrains.python.psi.PyAssignmentStatement
import com.jetbrains.python.psi.PyElementGenerator

/**
 * This class provides a solution to inspection not annotated variables by manipulating
 * the PSI tree to replace 'a = b' with 'a: int = b'
 */
class AssignmentAnnotationQuickFix : LocalQuickFix {

    companion object {
        /**
         * Defines the text of the quick fix intention
         */
        const val QUICK_FIX_NAME = "Add static type annotation"
        private val LOG = Logger.getInstance("#com.intellij.codeInspection.ComparingReferencesInspection")
    }

    /**
     * Returns a partially localized string for the quick fix intention.
     * Used by the test code for this plugin.
     *
     * @return Quick fix short name.
     */
    override fun getName(): String {
        return QUICK_FIX_NAME
    }

    /**
     * This method manipulates the PSI tree to replace
     * 'a = b' with 'a: int = b'
     *
     * @param project    The project that contains the file being edited.
     * @param descriptor A problem found by this inspection.
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
