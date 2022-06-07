package com.fastbuild.factory.generator.gen.ruoyi;

import com.fastbuild.factory.generator.common.FactoryConst;
import com.fastbuild.factory.generator.common.FileFormatter;
import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.IOException;

public class RuoyiMobileFormat extends AbstractFormat {

    private final String GEN_ID = "ruoyi#mobile";

    public RuoyiMobileFormat(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return FactoryConst.app.RUOYI.equals(app.getAppId())
                && FactoryConst.mobile.UNIAPP.equals(project.getMobileFramework());
    }

    @Override
    protected void dependency() throws Exception {
    }

    @Override
    protected void fileGenerator() throws Exception {
        String srcPath = properties.getFactoryRuoyiUniappPath();
        File srcFile = new File(srcPath);

        String destPath = project.getWorkPath() + File.separator + project.getMobileName();
        File destRoot = new File(destPath);

        FileUtils.copyDirectory(srcFile, destRoot, FileFilterUtils.trueFileFilter());

        this.fileContentFormat(destRoot);
    }

    private void fileContentFormat (File destRoot) throws IOException {
        FileFormatter cfgFormatter = new FileFormatter(destRoot, FileFilterUtils.nameFileFilter("ruoyi-config.js"));
        cfgFormatter.replaceAll("http://vue.ruoyi.vip/prod-api", "http://localhost:8080");
        cfgFormatter.format();

        if (FactoryConst.server.CLOUD.equals(project.getServerMode())) {
            FileFormatter loginFormatter = new FileFormatter(destRoot, FileFilterUtils.nameFileFilter("login.js"));
            loginFormatter.replaceAll("/login", "/auth/login");
            loginFormatter.replaceAll("/logout", "/auth/logout");
            loginFormatter.format();

            FileFormatter captchaFormatter = new FileFormatter(destRoot, FileFilterUtils.nameFileFilter("captcha.js"));
            captchaFormatter.replaceAll("/captchaImage", "/code");
            captchaFormatter.format();

            FileFormatter userFormatter = new FileFormatter(destRoot, FileFilterUtils.nameFileFilter("user.js"));
            userFormatter.replaceAll("/getInfo", "/system/user/getInfo");
            userFormatter.format();

            FileFormatter logFormatter = new FileFormatter(destRoot, FileFilterUtils.nameFileFilter("log.js"));
            logFormatter.replaceAll("/monitor/operlog/list", "/system/operlog/list");
            logFormatter.format();
        }
    }
}
