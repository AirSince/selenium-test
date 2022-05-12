package com.practice.web.Entity;

import lombok.Data;

import java.util.List;

/**
 *
 * 测试报告参数
 */
@Data
public class ReportInfo {
    private String name;
    private String className;
    private String methodName;
    private String description;
    private String spendTime;
    private String status;
    private List<String> log;
}

