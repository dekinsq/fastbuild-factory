package com.fastbuild.factory.generator.gen.yudao;

import com.fastbuild.factory.generator.common.FactoryConst;
import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;

public class YudaoCloudFormat extends AbstractFormat {

    private final String GEN_ID = "yudao#cloud";

    public YudaoCloudFormat(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return (FactoryConst.app.YUDAO.equals(app.getAppId())
                && !project.getGenList().contains(GEN_ID));
    }

    @Override
    protected void dependency() {
    }

    @Override
    public void gen() throws Exception {
        if (this.validate()) {
            this.dependency();
            this.fileGenerator();
            project.getGenList().add(GEN_ID);
        }
    }

    @Override
    protected void fileGenerator() {

    }
}
