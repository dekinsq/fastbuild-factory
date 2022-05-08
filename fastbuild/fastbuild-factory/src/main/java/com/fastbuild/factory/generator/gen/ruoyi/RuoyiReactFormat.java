package com.fastbuild.factory.generator.gen.ruoyi;

import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RuoyiReactFormat extends AbstractFormat {

    private final String GEN_ID = "ruoyi#react";

    public RuoyiReactFormat(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return "react".equals(project.getWebFramework());
    }

    @Override
    protected void dependency() {}

    @Override
    protected void fileGenerator() throws Exception {
    }
}
