package com.fastbuild.factory.generator.gen.ruoyi;

import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import com.fastbuild.factory.generator.gen.IGenHandler;

public class RuoyiHandler implements IGenHandler {

    private AppConfig app;

    private AbstractFormat[] genList;

    public RuoyiHandler(AppConfig app) {
        this.app = app;
        this.genList = new AbstractFormat[] {
                new RuoyiServerFormat(this.app),
                new RuoyiWebFormat(this.app),
                new RuoyiMobileFormat(this.app),
        };
    }

    @Override
    public void gen() throws Exception {
        for (AbstractFormat gen : genList) {
            gen.gen();
        }
    }

}
