package com.wxkly.lintmodule;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.ApiKt;
import com.android.tools.lint.detector.api.Issue;
import com.wxkly.lintmodule.detector.TextStringInCodeDetector;

import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import com.wxkly.lintmodule.detector.LogDetector;
import com.wxkly.lintmodule.detector.PngDetector;
import com.wxkly.lintmodule.detector.SampleCodeDetector;
import com.wxkly.lintmodule.detector.ThreadDetector;

/**
 * Created by Omooo
 * Date:2019-07-04
 */
@SuppressWarnings("UnstableApiUsage")
public class CustomIssueRegistry extends IssueRegistry {

    @NotNull
    @Override
    public List<Issue> getIssues() {
        return Arrays.asList(
                LogDetector.ISSUE,
                SampleCodeDetector.ISSUE,
                ThreadDetector.ISSUE,
                PngDetector.ISSUE_PNG_IN_XML,
                TextStringInCodeDetector.ISSUE,
                PngDetector.ISSUE_PNG_IN_CODE);
    }

    @Override
    public int getApi() {
        return ApiKt.CURRENT_API;
    }
}
