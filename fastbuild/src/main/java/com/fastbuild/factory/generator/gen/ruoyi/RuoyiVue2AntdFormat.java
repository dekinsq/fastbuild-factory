package com.fastbuild.factory.generator.gen.ruoyi;

import com.fastbuild.factory.generator.common.FactoryConst;
import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RuoyiVue2AntdFormat extends AbstractFormat {

    private final String GEN_ID = "ruoyi#vue2#antd";

    public RuoyiVue2AntdFormat(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return FactoryConst.web.VUE2.equals(project.getWebFramework()) && FactoryConst.ui.ANTD.equals(project.getWebUI());
    }

    @Override
    protected void dependency() {}

    @Override
    protected void fileGenerator() throws Exception {
        String srcPath = properties.getFactoryRuoyiVueAntdPath();
        String destPath = project.getWorkPath() + File.separator + project.getUiName();
        List<String> exclude = new ArrayList<>();
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
            if ("Navbar.vue".equals(file.getName())) {
                String content = FileUtils.readFileToString(file, "utf-8");
                content = content.replace("        <el-tooltip content=\"源码地址\" effect=\"dark\" placement=\"bottom\">", "");
                content = content.replace("          <ruo-yi-git id=\"ruoyi-git\" class=\"right-menu-item hover-effect\" />", "");

                content = content.replace("        <el-tooltip content=\"文档地址\" effect=\"dark\" placement=\"bottom\">", "");
                content = content.replace("          <ruo-yi-doc id=\"ruoyi-doc\" class=\"right-menu-item hover-effect\" />", "");

                content = content.replace("        <el-tooltip content=\"布局大小\" effect=\"dark\" placement=\"bottom\">", "");
                content = content.replace("          <size-select id=\"size-select\" class=\"right-menu-item hover-effect\" />", "");

                content = content.replaceAll("        </el-tooltip>", "");
                FileUtils.writeStringToFile(file, content.replace("<el-tooltip content=\"源码地址\" effect=\"dark\" placement=\"bottom\">", ""));
            } else {
                List<String> lines = FileUtils.readLines(file, "utf-8");
                List<String> newLines = new ArrayList<>();
                for (String text : lines) {
                    newLines.add(text.replaceAll("若依管理系统", project.getProjectTitle()).replaceAll("若依后台管理系统", project.getProjectTitle()));
                }
                FileUtils.writeLines(file, newLines, false);
            }
        }
    }
}
