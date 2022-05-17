package com.fastbuild.factory.generator.gen.yudao;

import com.fastbuild.factory.generator.common.FactoryConst;
import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import com.fastbuild.factory.generator.gen.ruoyi.RuoyiCloudFormat;
import com.fastbuild.factory.generator.gen.ruoyi.RuoyiSingleFormat;

import java.io.File;

public class YudaoOracleFormat extends AbstractFormat {

    private final String GEN_ID = "yudao#oracle";

    public YudaoOracleFormat(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return (FactoryConst.app.YUDAO.equals(app.getAppId())
                && FactoryConst.db.ORACLE.equals(project.getDatabase()));
    }

    @Override
    protected void dependency() throws Exception {
        new RuoyiSingleFormat(this.app).gen();
        new RuoyiCloudFormat(this.app).gen();
    }

    @Override
    protected void fileGenerator() throws Exception {
        File root = new File(project.getServerRootPath());
        new YudaoServerFormat(root, project).fileFormat();
    }
}
