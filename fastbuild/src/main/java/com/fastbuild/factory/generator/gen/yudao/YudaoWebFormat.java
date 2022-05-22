package com.fastbuild.factory.generator.gen.yudao;

import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YudaoWebFormat extends AbstractFormat {

    private final String GEN_ID = "ruoyi#vue2#element";

    public YudaoWebFormat(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return "vue2".equals(project.getWebFramework()) && "element".equals(project.getWebUI());
    }

    @Override
    protected void dependency() {}

    @Override
    protected void fileGenerator() throws Exception {
        String srcPath = properties.getFactoryYudaoVuePath() + File.separator + "yudao-ui-admin";
        String destPath = project.getWorkPath() + File.separator + project.getUiName();
        List<String> exclude = new ArrayList<>();
        exclude.add("node_modules");
        exclude.add("index_old.vue");
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
                newLines.add(text.replaceAll("芋道管理系统", project.getProjectTitle()));
            }
            FileUtils.writeLines(file, newLines, false);
        }
    }
}
