package com.github.khodand.pycharmplugin.inspection;

import com.intellij.codeInspection.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.ui.DocumentAdapter;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.python.PyStubElementTypes;
import com.jetbrains.python.psi.impl.PyAnnotationImpl;
import com.jetbrains.python.psi.impl.stubs.PyAnnotationElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import com.jetbrains.python.psi.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.util.StringTokenizer;

/**
 * Implements an inspection to detect when object references are compared using 'a==b' or 'a!=b'
 * The quick fix converts these comparisons to 'a.equals(b) or '!a.equals(b)' respectively.
 */
public class CodeInspection extends LocalInspectionTool {

    // Defines the text of the quick fix intention
    public static final String QUICK_FIX_NAME = "SDK: " +
            InspectionsBundle.message("inspection.comparing.references.use.quickfix");
    private static final Logger LOG = Logger.getInstance("#com.intellij.codeInspection.ComparingReferencesInspection");
    private final CriQuickFix myQuickFix = new CriQuickFix();
    // This string holds a list of classes relevant to this inspection.
    @SuppressWarnings({"WeakerAccess"})
    @NonNls
    public String CHECKED_CLASSES = "";

    @Override
    public JComponent createOptionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JTextField checkedClasses = new JTextField(CHECKED_CLASSES);
        checkedClasses.getDocument().addDocumentListener(new DocumentAdapter() {
            public void textChanged(@NotNull DocumentEvent event) {
                CHECKED_CLASSES = checkedClasses.getText();
            }
        });
        panel.add(checkedClasses);
        return panel;
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new PsiElementVisitor() {

            @NonNls
            private final String DESCRIPTION_TEMPLATE = "SDK " +
                    InspectionsBundle.message("inspection.comparing.references.problem.descriptor");

            //             public void visitElement(@NotNull PsiElement element) {
            //                ProgressIndicatorProvider.checkCanceled();
            //            }
            @Override
            public void visitElement(@NotNull PsiElement element) {
                super.visitElement(element);

                if (element instanceof PyAssignmentStatement) {
                    System.out.println(element.getText());
                    holder.registerProblem(element,
                            DESCRIPTION_TEMPLATE, myQuickFix);
                }
            }
        };
    }

    private static class CriQuickFix implements LocalQuickFix {
        @NotNull
        @Override
        public String getName() {
            return QUICK_FIX_NAME;
        }

        /**
         * This method manipulates the PSI tree to replace 'a==b' with 'a.equals(b)
         * or 'a!=b' with '!a.equals(b)'
         *
         * @param project    The project that contains the file being edited.
         * @param descriptor A problem found by this inspection.
         */
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            try {
                PyAssignmentStatement statement = (PyAssignmentStatement) descriptor.getPsiElement();

                if (!statement.getAnnotationValue().equals("")) {
                    return;
                }
                //PyStubElementType
                //PyAnnotationElementType
                //statement.addAfter(new PyAnnotationImpl(PyStubElementTypes.ANNOTATION), statement.getFirstChild());
            } catch (IncorrectOperationException e) {
                LOG.error(e);
            }
        }

        @NotNull
        public String getFamilyName() {
            return getName();
        }

    }

}
