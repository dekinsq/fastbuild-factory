package com.fastbuild.factory.generator.gen.yudao;

import com.fastbuild.factory.generator.common.FactoryConst;
import com.fastbuild.factory.generator.domain.ProjectConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.CaseUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YudaoServerFormat {

    private File root;

    private ProjectConfig project;

    public YudaoServerFormat(File root, ProjectConfig project) {
        this.root = root;
        this.project = project;
    }

    public void fileFormat() throws Exception {
        // 替换包名
        List<File> fileList = (List<File>) FileUtils.listFiles(root, null, true);
        final String classNamePrefix = CaseUtils.toCamelCase(project.getProjectName(), true, new char[] { '-', '_' });
        final String varNamePrefix = CaseUtils.toCamelCase(project.getProjectName(), false, new char[] { '-', '_' });
        for (File file : fileList) {
            if (file.isFile()) {
                this.writeServerFile(file, classNamePrefix, varNamePrefix);
            }
        }
    }

    private void writeServerFile(File file, String classNamePrefix, String varNamePrefix) throws Exception {
        if ("banner.txt".equals(file.getName())) {
            FileUtils.write(file, "Application Version: ${" + varNamePrefix + ".version}\n");
            FileUtils.write(file, "Spring Boot Version: ${spring-boot.version}", true);
            return;
        }
        List<String> lines = FileUtils.readLines(file, "utf-8");
        List<String> newLines = new ArrayList<>();
        for (String text : lines) {
            String line = text.replaceAll("cn.iocoder.yudao", project.getPackagePrefix())
                    .replaceAll("芋道管理系统", project.getProjectTitle());
            if (file.getName().endsWith("java") || file.getName().endsWith("xml")) {
                line = line.replaceAll("YuDao", classNamePrefix)
                        .replaceAll("Yudao", classNamePrefix)
                        .replaceAll("yudao", varNamePrefix);
            } else if (file.getName().endsWith("yml") || file.getName().endsWith("yaml")) {
                if ("application-local.yaml".equals(file.getName())) {
                    String regex = "jdbc:mysql://127.0.0.1:3306/${spring.datasource.dynamic.datasource.master.name}?allowMultiQueries=true&useUnicode=true&useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&autoReconnect=true # MySQL Connector/J 8.X 连接的示例";
                    if (FactoryConst.db.ORACLE.equals(project.getDatabase())) {
                        String replacement = "jdbc:oracle:thin:@127.0.0.1:1521:${spring.datasource.dynamic.datasource.master.name} # Oracle 连接的示例";
                        line = line.replace(regex, replacement);
                    } else if (FactoryConst.db.SQL_SERVER.equals(project.getDatabase())) {
                        String replacement = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=${spring.datasource.dynamic.datasource.master.name} # SQLServer 连接的示例";
                        line = line.replaceAll(regex, "");
                    }
                }
                line = line.replaceAll("name: yudao-server", "name: " + project.getProjectName())
                        .replaceAll("yudao", varNamePrefix)
                        .replaceAll("/yudao/", project.getProjectName());
            } else if (file.getName().endsWith("sql")) {
                line = text.replaceAll("cn.iocoder.yudao", project.getPackagePrefix());
            } else if (file.getName().endsWith("factories")) {
                line = text.replaceAll("cn.iocoder.yudao", project.getPackagePrefix())
                        .replaceAll("YuDao", classNamePrefix)
                        .replaceAll("Yudao", classNamePrefix);
            } else if (file.getName().endsWith("md")) {
                line = text.replaceAll("yudao", varNamePrefix);
            }
            newLines.add(line);
        }
        FileUtils.writeLines(file, newLines, false);
        if (file.getName().endsWith("java") && file.getName().contains("Yudao")) {
            file.renameTo(new File(file.getParent() + File.separator + file.getName().replaceAll("Yudao", classNamePrefix)));
        }
    }

}
