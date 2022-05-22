package com.fastbuild.factory.generator.gen.ruoyi;

import com.fastbuild.factory.generator.common.FactoryConst;
import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;

/**
 * 若依微服务版本格式化
 *
 * @author fastbuild@163.com
 */
public class RuoyiCloudFormat extends AbstractFormat {

    private final String GEN_ID = "ruoyi#cloud";

    public RuoyiCloudFormat(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return FactoryConst.app.RUOYI.equals(app.getAppId()) && FactoryConst.server.CLOUD.equals(project.getServerMode());
    }

    @Override
    protected void dependency() {
    }

    @Override
    protected void fileGenerator() {

    }
}
