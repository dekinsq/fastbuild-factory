package com.fastbuild.factory.generator.domain;

import lombok.Data;
import lombok.ToString;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@ToString
public class ProjectConfig {

    // 用户sessionid
    private String sessionId;

    // 工作目录
    private String workPath;

    // 项目id
    private String projectId;

    // 项目标题
    private String projectTitle;

    // 项目名称
    private String projectName;

    // 作者
    private String author;

    // 描述
    private String desc;

    // 前端框架
    private String uiName;

    private String webFramework;

    private String webUI;

    private String mobileFramework;

    private String serverName;

    private String serverMode;

    private String language;

    private String languageVersion;

    private String packagePrefix;

    private String database;

    private String databaseVersion;

    private String swagger;

    private List<AppConfig> appList;

    private FactoryProperties properties;

    private Set<String> genList = new HashSet<>();

    public String getUiName() {
        return this.projectName + "-ui";
    }

    public String getServerRootPath() {
        return this.workPath + File.separator + this.projectName;
    }

}
