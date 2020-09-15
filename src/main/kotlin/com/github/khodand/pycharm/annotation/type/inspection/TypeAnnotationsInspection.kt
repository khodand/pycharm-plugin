package com.github.khodand.pycharm.annotation.type.inspection

import com.github.khodand.pycharm.annotation.type.inspection.quickFixes.AssignmentAnnotationQuickFix
import com.github.khodand.pycharm.annotation.type.inspection.quickFixes.FunctionAnnotationQuickFix
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
     * This method is overridden to provide a custom visitor
     * that inspects expressions with assignments or functions declarations without annotations.
     * The visitor must not be recursive and must be thread-safe.
     *
     * @param holder object for visitor to register problems found.
     * @param isOnTheFly true if inspection was run in non-batch mode.
     * @return non-null visitor for this inspection.
     * @see PyElementVisitor
     */
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PyElementVisitor {
        return object : PyElementVisitor() {
            @NonNls
            private val DESCRIPTION = "No any type annotations."

            /**
             * Visits all PsiElements and checks are they instances
             * of PyAssignmentStatement or PyFunction classes
             * then have they annotation or not.
             *
             * @param element  The PsiElement to be checked.
             */
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
