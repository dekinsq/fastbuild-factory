package com.fastbuild.factory.generator.gen.ruoyi;

import com.fastbuild.factory.generator.common.FactoryConst;
import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import com.fastbuild.factory.generator.gen.common.FileFormatter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.IOException;

public class RuoyiWebFormat extends AbstractFormat {

    private final String GEN_ID = "ruoyi#web";

    public RuoyiWebFormat(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return FactoryConst.app.RUOYI.equals(app.getAppId())
                && !FactoryConst.web.THYMELEAF.equals(project.getWebFramework());
    }

    @Override
    protected void dependency() throws Exception {
    }

    @Override
    protected void fileGenerator() throws Exception {
        String srcPath = this.getSrcPath();
        File srcFile = new File(srcPath);

        String destPath = project.getWorkPath() + File.separator + project.getUiName();
        File destRoot = new File(destPath);

        FileUtils.copyDirectory(srcFile, destRoot, FileFilterUtils.trueFileFilter());

        this.fileContentFormat(destRoot);
    }

    private void fileContentFormat (File destRoot) throws IOException {
        FileFormatter vueFormatter = new FileFormatter(destRoot, FileFilterUtils.suffixFileFilter("vue"));
        vueFormatter.replaceAll("若依管理系统", project.getProjectTitle());
        vueFormatter.replaceAll("若依后台管理系统", project.getProjectTitle());
        vueFormatter.format();

        FileFormatter developmentFormatter = new FileFormatter(destRoot, FileFilterUtils.suffixFileFilter("development"));
        developmentFormatter.replaceAll("若依管理系统", project.getProjectTitle());
        developmentFormatter.replaceAll("若依后台管理系统", project.getProjectTitle());
        developmentFormatter.format();

        FileFormatter productionFormatter = new FileFormatter(destRoot, FileFilterUtils.suffixFileFilter("production"));
        productionFormatter.replaceAll("若依管理系统", project.getProjectTitle());
        productionFormatter.replaceAll("若依后台管理系统", project.getProjectTitle());
        productionFormatter.format();
    }

    private String getSrcPath () {
        if (FactoryConst.server.CLOUD.equals(this.project.getServerMode())) {
            return properties.getFactoryRuoyiCloudPath() + File.separator + "ruoyi-ui";
        } else if (FactoryConst.web.VUE2.equals(project.getWebFramework()) && FactoryConst.ui.ELEMENT.equals(project.getWebUI())) {
            return properties.getFactoryRuoyiVuePath() + File.separator + "ruoyi-ui";
        } else if (FactoryConst.web.VUE3.equals(project.getWebFramework()) && FactoryConst.ui.ELEMENT.equals(project.getWebUI())) {
            return properties.getFactoryRuoyiVue3Path();
        } else if (FactoryConst.web.VUE2.equals(project.getWebFramework()) && FactoryConst.ui.ANTD.equals(project.getWebUI())) {
            return properties.getFactoryRuoyiVueAntdPath();
        } else if (FactoryConst.web.VUE3.equals(project.getWebFramework()) && FactoryConst.ui.ANTD.equals(project.getWebUI())) {
            return properties.getFactoryRuoyiVue3AntdPath();
        }
        return null;
    }
}
