package com.fastbuild.factory.generator.gen.ruoyi;

import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RuoyiVue3Format extends AbstractFormat {

    private final String GEN_ID = "ruoyi#vue3";

    public RuoyiVue3Format(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return "vue3".equals(project.getWebFramework());
    }

    @Override
    protected void dependency() {}

    @Override
    protected void fileGenerator() throws Exception {
        String srcPath = properties.getFactoryRuoyiVue3Path();
        String destPath = project.getWorkPath() + File.separator + project.getUiName();
        List<String> exclude = new ArrayList<>();
        exclude.add("bin");
        exclude.add("README.md");
        FileUtils.copyDirectory(new File(srcPath), new File(destPath), new FileFileFilter() {
            @Override
            public boolean accept(File file) {
                String fileName = file.getName();
                return !exclude.contains(fileName);
            }
        });
        String[] suffix = new String[]{ "env.development", "env.production", "env.staging", "json", "vue", "js" };
        List<File> fileList = (List<File>) FileUtils.listFiles(new File(destPath), suffix, true);

        for (File file : fileList) {
            List<String> lines = FileUtils.readLines(file, "utf-8");
            List<String> newLines = new ArrayList<>();
            for (String text : lines) {
                newLines.add(text.replaceAll("若依管理系统", project.getProjectTitle()).replaceAll("若依后台管理系统", project.getProjectTitle()));
            }
            FileUtils.writeLines(file, newLines, false);
        }
    }
}
