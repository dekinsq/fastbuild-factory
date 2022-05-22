package com.fastbuild.factory.generator.gen.yudao;

import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import com.fastbuild.factory.generator.gen.IGenHandler;

public class YudaoHandler implements IGenHandler {

    private AppConfig app;

    private AbstractFormat[] genList;

    public YudaoHandler(AppConfig app) {
        this.app = app;
        genList = new AbstractFormat[] {
            new YudaoSingleFormat(this.app),
            new YudaoCloudFormat(this.app),

            new YudaoWebFormat(this.app),
        };
    }

    @Override
    public void gen() throws Exception {
        for (AbstractFormat gen : genList) {
            gen.gen();
        }
    }

}
