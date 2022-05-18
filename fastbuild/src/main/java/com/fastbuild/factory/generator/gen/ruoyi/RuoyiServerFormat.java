package com.fastbuild.factory.generator.gen.ruoyi;

import com.fastbuild.factory.generator.domain.ProjectConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.CaseUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RuoyiServerFormat {

    private File root;

    private ProjectConfig project;

    public RuoyiServerFormat (File root, ProjectConfig project) {
        this.root = root;
        this.project = project;
    }

    public void fileFormat() throws Exception {
        // 替换包名
        String[] suffix = new String[]{ "java", "yml", "xml", "vm", "sql", "txt" };
        List<File> fileList = (List<File>) FileUtils.listFiles(root, suffix, true);
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
            String line = text.replaceAll("com.ruoyi", project.getPackagePrefix())
                    .replaceAll("若依管理系统", project.getProjectTitle());
            if (file.getName().endsWith("java")) {
                line = line.replaceAll("RuoYi", classNamePrefix)
                        .replaceAll("Ruoyi", classNamePrefix)
                        .replaceAll("ruoyi\\.", varNamePrefix + ".")
                        .replaceAll("prefix = \"ruoyi\"", "prefix = \"" + varNamePrefix + "\"");
                if ("RuoYiApplication.java".equals(file.getName())) {
                    line = line.replace("(♥◠‿◠)ﾉﾞ  若依启动成功   ლ(´ڡ`ლ)ﾞ  \\n\" +", project.getProjectTitle() + " 启动成功!!\");");
                    if (line.contains("\\n\" +") || line.contains("`-..-'")) {
                        continue;
                    }
                }
            } else if ("pom.xml".equals(file.getName())) {
                line = line.replaceAll("ruoyi", project.getProjectName());
            } else if (file.getName().endsWith("yml")) {
                line = line.replaceAll("name: RuoYi", "name: " + project.getProjectName())
                        .replaceAll("ry-vue", project.getProjectName())
                        .replaceAll("ruoyi", varNamePrefix)
                        .replaceAll("/ruoyi/", project.getProjectName());
            } else if (file.getName().endsWith("sql")) {
                line = text.replaceAll("ruoyi", varNamePrefix)
                        .replaceAll("若依", project.getProjectName());
            }
            newLines.add(line);
        }
        FileUtils.writeLines(file, newLines, false);
        if (file.getName().endsWith("java") && file.getName().contains("RuoYi")) {
            file.renameTo(new File(file.getParent() + File.separator + file.getName().replaceAll("RuoYi", classNamePrefix)));
        }
    }

}
