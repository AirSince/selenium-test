package com.practice.web.listener;

import com.alibaba.fastjson.JSON;
import com.practice.web.Entity.ReportInfo;
import com.practice.web.annotation.MethodDescription;
import org.apache.logging.log4j.util.Strings;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;

public class MateTestListener extends RunListener {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    ClassLoader classLoader = getClass().getClassLoader();
    private String templatePath = classLoader.getResource("report.template").getFile();
    private String testName;
    private long testStartTime;
    private long testFinishTime;
    private ConcurrentHashMap<String, Long> testIdentifierStartTime = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Long> testIdentifierFinishTime = new ConcurrentHashMap<>();
    final AtomicLong testsStarted = new AtomicLong();
    final AtomicLong testsSkipped = new AtomicLong();
    final AtomicLong testsSucceeded = new AtomicLong();
    final AtomicLong testsFailed = new AtomicLong();
    private ConcurrentHashMap<String, String> methodDescription = new ConcurrentHashMap<>();
    private List<ReportInfo> reportInfos = new ArrayList<>();
    private Set<String> fieldMethod = new HashSet<>();

    @Override
    public void testRunStarted(Description description) throws Exception {
        this.testStartTime = System.currentTimeMillis();
        String[] strArray = description.getClassName().split("\\.");
        this.testName = strArray[strArray.length - 1];
        // 创建文件夹
        File fileMkdir = new File(testName);
        if (!fileMkdir.exists())
            fileMkdir.mkdir();
        super.testRunStarted(description);
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        this.testFinishTime = System.currentTimeMillis();
        outputResult(testName + "/");
        super.testRunFinished(result);
    }

    // 方法开始
    @Override
    public void testStarted(Description description) throws Exception {
        testIdentifierStartTime.put(description.getDisplayName(), System.currentTimeMillis());
        testsStarted.incrementAndGet();
        methodDescription.put(description.getDisplayName(), getDescription(description));
        super.testStarted(description);
    }

    // 方法结束
    @Override
    public void testFinished(Description description) throws Exception {
        testIdentifierFinishTime.put(description.getDisplayName(), System.currentTimeMillis());
        if (!fieldMethod.contains(description.getDisplayName())) {
            testsSucceeded.incrementAndGet();
            addReportInfo(description, "SUCCESS", null);
        }
        super.testFinished(description);
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        String name = failure.getDescription().getDisplayName();
        testIdentifierFinishTime.put(name, System.currentTimeMillis());
        fieldMethod.add(name);
        testsFailed.incrementAndGet();
        addReportInfo(failure.getDescription(), "FAILED", failure.getException());
        // 删除失败方法的截图
        File file = new File(testName+"/"+failure.getDescription().getMethodName()+".jpg");
        file.delete();
        super.testFailure(failure);
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        super.testAssumptionFailure(failure);
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        testsStarted.incrementAndGet();
        testsSkipped.incrementAndGet();
        methodDescription.put(description.getDisplayName(), getDescription(description));
        addReportInfo(description, "IGNORE", null);
        super.testIgnored(description);
    }


    private void addReportInfo(Description description, String status, Throwable throwable) {
        ReportInfo info = new ReportInfo();
        String uniqueId = description.getDisplayName();
        String displayName = description.getDisplayName();
        long spendTime = 0;
        if (!"IGNORE".equals(status)) {
            Long startTime = testIdentifierStartTime.get(uniqueId);
            Long finishTime = testIdentifierFinishTime.get(uniqueId);
            spendTime = finishTime - startTime;
        }
        String statusDesc = this.getStatus(status);
        List<String> logs = new ArrayList<>();

        if (throwable != null) {
            logs.add(this.toHtml(throwable.toString()));
            StackTraceElement[] st = throwable.getStackTrace();
            for (StackTraceElement stackTraceElement : st) {
                logs.add(this.toHtml("    " + stackTraceElement));
            }
        }

        info.setName(displayName);
        info.setSpendTime(spendTime + "ms");
        info.setStatus(statusDesc);
        info.setClassName(description.getClassName());
        info.setMethodName(description.getMethodName());
        String methodDes = methodDescription.get(description.getDisplayName());
        if (Strings.isEmpty(methodDes))
            methodDes = description.getClassName() + "." + description.getMethodName() + "()";
        info.setDescription(methodDes);
        info.setLog(logs);
        reportInfos.add(info);
    }

    /**
     * 输出report.html
     *
     * @param reporterDir html报告所在的目录
     */
    public void outputResult(String reporterDir) {
        try {
            File file = new File(reporterDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            String destPath = reporterDir + "testReport.html";

            String testPlanBeginTime = simpleDateFormat.format(testStartTime);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("testName", testName);
            result.put("testPass", testsSucceeded.get());
            result.put("testFail", testsFailed.get());
            result.put("testSkip", testsSkipped.get());
            result.put("testAll", testsStarted);
            result.put("beginTime", testPlanBeginTime);
            result.put("totalTime", (testFinishTime - testStartTime) + "ms");
            result.put("testResult", reportInfos);
            if (templatePath == null) {
                throw new Exception("保存路径不能为空");
            }
            String template = this.read(templatePath);
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(destPath)), "UTF-8"));
            String jsonString = JSON.toJSONString(result);
            template = template.replaceFirst("\\$\\{resultData\\}", Matcher.quoteReplacement(jsonString));
            output.write(template);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取方法描述
     * @param description
     * @return
     */
    private String getDescription(Description description) {
        MethodDescription methodDes = description.getAnnotation(MethodDescription.class);
        if (methodDes == null)
            return description.getClassName() + "." + description.getMethodName() + "()";
        return description.getAnnotation(MethodDescription.class).value();
    }

    private String getStatus(String status) {
        String statusString = null;
        switch (status) {
            case "SUCCESS":
                statusString = "成功";
                break;
            case "FAILED":
                statusString = "失败";
                break;
            case "IGNORE":
                statusString = "跳过";
                break;
        }
        return statusString;
    }

    private String toHtml(String str) {
        if (str == null) {
            return "";
        } else {
            str = str.replaceAll("<", "&lt;");
            str = str.replaceAll(">", "&gt;");
            str = str.replaceAll(" ", "&nbsp;");
            str = str.replaceAll("\n", "<br>");
            str = str.replaceAll("\"", "\\\\\"");
        }
        return str;
    }

    /**
     * 读取报告模版
     *
     * @param path
     * @return
     */
    private String read(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr + "\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }
}
