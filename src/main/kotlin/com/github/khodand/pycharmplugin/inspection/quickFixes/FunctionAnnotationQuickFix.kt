package com.github.khodand.pycharmplugin.inspection.quickFixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiComment
import com.intellij.util.IncorrectOperationException
import com.jetbrains.python.psi.LanguageLevel
import com.jetbrains.python.psi.PyAssignmentStatement
import com.jetbrains.python.psi.PyElementGenerator
import com.jetbrains.python.psi.PyFunction

public class FunctionAnnotationQuickFix : LocalQuickFix {
    companion object {
        const val QUICK_FIX_NAME = "SDK: Add static type annotation"
        private val LOG = Logger.getInstance("#com.intellij.codeInspection.ComparingReferencesInspection")
    }

    override fun getName(): String {
        return QUICK_FIX_NAME
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        try {
            if (descriptor.psiElement !is PyFunction)
                return

            val function = descriptor.psiElement as PyFunction
            val annotatedFunction = PyElementGenerator.getInstance(project)
                    .createFromText(LanguageLevel.PYTHON38, PyFunction::class.java
                            , "def a() -> int:  # hello!\n print(\"Hello World!\")")

            function.nameIdentifier?.let { annotatedFunction.nameIdentifier!!.replace(it) }
            annotatedFunction.parameterList.replace(function.parameterList)
            annotatedFunction.statementList.replace(function.statementList)
            if (function.lastChild.prevSibling.prevSibling is PsiComment) {
                 annotatedFunction.lastChild.prevSibling.prevSibling.replace(function.lastChild.prevSibling.prevSibling)
            }
            else {
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
