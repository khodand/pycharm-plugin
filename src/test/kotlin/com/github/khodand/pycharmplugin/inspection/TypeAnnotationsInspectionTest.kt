package com.github.khodand.pycharmplugin.inspection

import com.github.khodand.pycharmplugin.inspection.quickFixes.AssignmentAnnotationQuickFix
import com.github.khodand.pycharmplugin.inspection.quickFixes.FunctionAnnotationQuickFix
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import org.junit.Test


internal class TypeAnnotationsInspectionTest : LightPlatformCodeInsightFixture4TestCase() {
    override fun getTestDataPath(): String? {
        return "src/test/testData"
    }

    private fun doTest(testName: String, fixName: String) {
        myFixture.configureByFile("$testName.py")
        myFixture.enableInspections(TypeAnnotationsInspection())
        val highlightInfos = myFixture.doHighlighting()
        assertFalse(highlightInfos.isEmpty())
        val action = myFixture.findSingleIntention(fixName)
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResultByFile("$testName.expected.py")
    }

    @Test
    fun testSimpleAssignmentAnnotation() {
        doTest("assignment", AssignmentAnnotationQuickFix.QUICK_FIX_NAME)
    }

    @Test
    fun testCommentedAssignmentAnnotation() {
        doTest("assignment_commented", AssignmentAnnotationQuickFix.QUICK_FIX_NAME)
    }

    @Test
    fun testSimpleFunctionAnnotation() {
        doTest("function", FunctionAnnotationQuickFix.QUICK_FIX_NAME)
    }

    @Test
    fun testCommentedFunctionAnnotation() {
        doTest("function_commented", FunctionAnnotationQuickFix.QUICK_FIX_NAME)
    }
}
