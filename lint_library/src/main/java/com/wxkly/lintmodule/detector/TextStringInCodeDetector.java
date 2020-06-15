package com.wxkly.lintmodule.detector;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UExpression;

import java.util.Arrays;
import java.util.List;


/**
 * Created by wxkly.
 * Date: 20-6-2
 * Description:
 */

public class TextStringInCodeDetector extends Detector implements Detector.UastScanner {


    public static final Issue ISSUE = Issue.create(
            "stringNotTranslate",
            "String in code",
            "do not directly write string in code",
            Category.LINT, 5, Severity.ERROR,
            new Implementation(TextStringInCodeDetector.class, Scope.JAVA_FILE_SCOPE));


    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("setText");
    }


    @Override
    public void visitMethodCall(@NotNull JavaContext context,
                                @NotNull UCallExpression node,
                                @NotNull PsiMethod method) {
        System.out.println("node.asRenderString()"+node.asRenderString());
        System.out.println("node.asSourceString()"+node.asSourceString());
        if(node.getValueArguments().size()==1) {
            UExpression uExpression =node.getValueArguments().get(0);
            if(uExpression.asRenderString().startsWith("\"")){
                context.report(ISSUE, node, context.getLocation(node),
                        "String in code！");
            }
        }


//        if (!UastExpressionUtils.isMethodCall(node)) return;
//        if (node.getReceiver() != null
//                && node.getMethodName() != null) {
//            String methodName = node.getMethodName();
//            if (methodName.equals("i")
//                    || methodName.equals("d")
//                    || methodName.equals("e")
//                    || methodName.equals("v")
//                    || methodName.equals("w")
//                    || methodName.equals("wtf")) {
//                PsiMethod method = node.resolve();
//                if (context.getEvaluator().isMemberInClass(method, "android.util.Log")) {
//                    reportAllocation(context, node);
//                }
//            }
//        }
    }
}