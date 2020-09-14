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

    // This string holds a list of classes relevant to this inspection.
//    @NonNls
//    var CHECKED_CLASSES = ""
//    override fun createOptionsPanel(): JComponent? {
//        val panel = JPanel(FlowLayout(FlowLayout.LEFT))
//        val checkedClasses = JTextField(CHECKED_CLASSES)
//        checkedClasses.document.addDocumentListener(object : DocumentAdapter() {
//            public override fun textChanged(event: DocumentEvent) {
//                CHECKED_CLASSES = checkedClasses.text
//            }
//        })
//        panel.add(checkedClasses)
//        return panel
//    }

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
                    println(element.getText())
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

        /**
         * This method manipulates the PSI tree to replace 'a==b' with 'a.equals(b)
         * or 'a!=b' with '!a.equals(b)'
         *
         * @param project    The project that contains the file being edited.
         * @param descriptor A problem found by this inspection.
         */
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
        val QUICK_FIX_NAME = "SDK: " +
                InspectionsBundle.message("inspection.comparing.references.use.quickfix")
        private val LOG = Logger.getInstance("#com.intellij.codeInspection.ComparingReferencesInspection")
    }
}