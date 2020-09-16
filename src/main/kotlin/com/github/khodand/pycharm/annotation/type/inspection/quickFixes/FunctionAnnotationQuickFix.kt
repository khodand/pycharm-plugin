package com.github.khodand.pycharm.annotation.type.inspection.quickFixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiComment
import com.intellij.util.IncorrectOperationException
import com.jetbrains.python.psi.LanguageLevel
import com.jetbrains.python.psi.PyElementGenerator
import com.jetbrains.python.psi.PyFunction

/**
 * This class provides a solution to inspection not annotated functions by manipulating
 * the PSI tree to replace 'def myFun():' with 'def myFun() -> int:'
 */
class FunctionAnnotationQuickFix : LocalQuickFix {

    companion object {
        /**
         * Defines the text of the quick fix intention
         */
        const val QUICK_FIX_NAME = "Declare return type with annotation"
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
     * 'def myFun():' with 'def myFun() -> int:'
     *
     * @param project    The project that contains the file being edited.
     * @param descriptor A problem found by this inspection.
     */
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        try {
            if (descriptor.psiElement !is PyFunction) {
                return
            }

            val function = descriptor.psiElement as PyFunction
            val annotatedFunction = PyElementGenerator.getInstance(project).createFromText(
                LanguageLevel.PYTHON38,
                PyFunction::class.java,
                "def a() -> int:  #smth\n print(\"Hello World!\")"
            )

            function.nameIdentifier?.let { annotatedFunction.nameIdentifier!!.replace(it) }
            annotatedFunction.parameterList.replace(function.parameterList)
            annotatedFunction.statementList.replace(function.statementList)
            if (function.lastChild.prevSibling.prevSibling is PsiComment) {
                annotatedFunction.lastChild.prevSibling.prevSibling.replace(function.lastChild.prevSibling.prevSibling)
            } else {
                annotatedFunction.lastChild.prevSibling.prevSibling.delete()
            }
            function.replace(annotatedFunction)
        } catch (e: IncorrectOperationException) {
            LOG.error(e)
        }
    }

    override fun getFamilyName(): String {
        return name
    }
}
