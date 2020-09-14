package com.github.khodand.pycharmplugin.inspection

import com.github.khodand.pycharmplugin.inspection.quickFixes.AssignmentAnnotationQuickFix
import com.github.khodand.pycharmplugin.inspection.quickFixes.FunctionAnnotationQuickFix
import com.intellij.codeInspection.*
import com.intellij.psi.PsiElement
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.psi.PyAssignmentStatement
import com.jetbrains.python.psi.PyElementVisitor
import com.jetbrains.python.psi.PyFunction
import org.jetbrains.annotations.NonNls


class TypeAnnotationsInspection : PyInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PyElementVisitor {
        return object : PyElementVisitor() {
            @NonNls
            private val DESCRIPTION_TEMPLATE = "SDK adsfafrea"

            override fun visitElement(element: PsiElement) {
                super.visitElement(element)

                if (element is PyAssignmentStatement) {
                    if (element.annotationValue != null) {
                        return
                    }
                    holder.registerProblem(element,
                            DESCRIPTION_TEMPLATE, AssignmentAnnotationQuickFix())
                }

                if (element is PyFunction) {
                    if (element.annotationValue != null) {
                        return
                    }
                    holder.registerProblem(element,
                            DESCRIPTION_TEMPLATE, FunctionAnnotationQuickFix())
                }
            }
        }
    }

}