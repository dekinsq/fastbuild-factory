package com.fastbuild.factory.generator.gen.ruoyi;

import com.fastbuild.factory.generator.common.FactoryConst;
import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;

public class RuoyiCloudFormat extends AbstractFormat {

    private final String GEN_ID = "ruoyi#springboot";

    public RuoyiCloudFormat(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return (FactoryConst.app.RUOYI.equals(app.getAppId())
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
