package com.fastbuild.factory.generator.gen.yudao;

import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import com.fastbuild.factory.generator.gen.IGenHandler;
import com.fastbuild.factory.generator.gen.ruoyi.*;

public class YudaoHandler implements IGenHandler {

    private AppConfig app;

    private AbstractFormat[] genList;

    public YudaoHandler(AppConfig app) {
        this.app = app;
        genList = new AbstractFormat[] {
            new YudaoSingleFormat(this.app),
            new YudaoCloudFormat(this.app),

            new YudaoMySqlFormat(this.app),
            new YudaoOracleFormat(this.app),
            new YudaoSqlServerFormat(this.app),

            new YudaoVue2Format(this.app),
        };
    }

    @Override
    public void gen() throws Exception {
        for (AbstractFormat gen : genList) {
            gen.gen();
        }
    }

}
