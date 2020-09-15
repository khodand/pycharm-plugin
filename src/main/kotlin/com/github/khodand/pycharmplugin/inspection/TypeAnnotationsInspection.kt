package com.github.khodand.pycharmplugin.inspection

import com.github.khodand.pycharmplugin.inspection.quickFixes.AssignmentAnnotationQuickFix
import com.github.khodand.pycharmplugin.inspection.quickFixes.FunctionAnnotationQuickFix
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.psi.PyAssignmentStatement
import com.jetbrains.python.psi.PyElementVisitor
import com.jetbrains.python.psi.PyFunction
import org.jetbrains.annotations.NonNls

/**
 * Implements an inspections to detect when a python variable or a function is assigned without the type specified
 * in the annotation. Like 'a = b' or 'def myFun():' And proposes fix for the issue, like 'a: int = b' or
 * 'def myFun() -> int:' respectively.
 * For now, just int type supported because it is a stub for a future plugin.
 */
class TypeAnnotationsInspection : PyInspection() {

    /**
     *
     */
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PyElementVisitor {
        return object : PyElementVisitor() {
            @NonNls
            private val DESCRIPTION = "No any type annotations."

            override fun visitElement(element: PsiElement) {
                super.visitElement(element)
                if (element.language != Language.findLanguageByID("Python")) {
                    return
                }

                if (element is PyAssignmentStatement && element.annotationValue == null) {
                    holder.registerProblem(element, DESCRIPTION, AssignmentAnnotationQuickFix())
                } else if (element is PyFunction && element.annotationValue == null) {
                    holder.registerProblem(element, DESCRIPTION, FunctionAnnotationQuickFix())
                }
            }
        }
    }
}
