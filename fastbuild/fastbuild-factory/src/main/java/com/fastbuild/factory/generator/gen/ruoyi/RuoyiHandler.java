package com.fastbuild.factory.generator.gen.ruoyi;

import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import com.fastbuild.factory.generator.gen.IGenHandler;

public class RuoyiHandler implements IGenHandler {

    private AppConfig app;

    private AbstractFormat[] genList;

    public RuoyiHandler(AppConfig app) {
        this.app = app;
        genList = new AbstractFormat[] {
            new RuoyiSingleFormat(this.app),
            new RuoyiCloudFormat(this.app),

            new RuoyiVue2Format(this.app),
            new RuoyiVue3Format(this.app),
            new RuoyiReactFormat(this.app),

            new RuoyiMySqlFormat(this.app),
            new RuoyiOracleFormat(this.app),
            new RuoyiSqlServerFormat(this.app)
        };
    }

    @Override
    public void gen() throws Exception {
        for (AbstractFormat gen : genList) {
            gen.gen();
        }
    }

}
