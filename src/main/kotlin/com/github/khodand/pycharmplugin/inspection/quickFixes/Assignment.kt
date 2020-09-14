package com.github.khodand.pycharmplugin.inspection.quickFixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.IncorrectOperationException
import com.jetbrains.python.psi.PyAssignmentStatement

public class AssignmentAnnotationQuickFix : LocalQuickFix {
    companion object {
        // Defines the text of the quick fix intention
        val QUICK_FIX_NAME = "SDK: Helo boy"
        private val LOG = Logger.getInstance("#com.intellij.codeInspection.ComparingReferencesInspection")
    }

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