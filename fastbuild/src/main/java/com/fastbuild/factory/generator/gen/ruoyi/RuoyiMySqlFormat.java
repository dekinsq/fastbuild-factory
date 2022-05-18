package com.fastbuild.factory.generator.gen.ruoyi;

import com.fastbuild.factory.generator.common.FactoryConst;
import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;

import java.io.File;

public class RuoyiMySqlFormat extends AbstractFormat {

    private final String GEN_ID = "ruoyi#mysql";

    public RuoyiMySqlFormat(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return (FactoryConst.app.RUOYI.equals(app.getAppId())
                && FactoryConst.db.MYSQL.equals(project.getDatabase()));
    }

    @Override
    protected void dependency() throws Exception {
        new RuoyiSingleFormat(this.app).gen();
        new RuoyiCloudFormat(this.app).gen();
    }

    @Override
    protected void fileGenerator() throws Exception {
        File root = new File(project.getServerRootPath());
        new RuoyiServerFormat(root, project).fileFormat();
    }
}
