package com.fastbuild.factory.generator.gen.jeecg;

import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import com.fastbuild.factory.generator.gen.IGenHandler;

public class JeecgHandler implements IGenHandler {

    private AppConfig app;

    private AbstractFormat[] genList;

    public JeecgHandler(AppConfig app) {
        this.app = app;
        genList = new AbstractFormat[] {
            new JeecgSingleFormat(this.app),
        };
    }

    @Override
    public void gen() throws Exception {
        for (AbstractFormat gen : genList) {
            gen.gen();
        }
    }

}
