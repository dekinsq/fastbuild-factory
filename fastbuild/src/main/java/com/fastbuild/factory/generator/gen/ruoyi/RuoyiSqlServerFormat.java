package com.fastbuild.factory.generator.gen.ruoyi;

import com.fastbuild.factory.generator.common.FactoryConst;
import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuoyiSqlServerFormat extends AbstractFormat {

    private final String GEN_ID = "ruoyi#sqlserver";

    public RuoyiSqlServerFormat(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return FactoryConst.db.SQL_SERVER.equals(project.getDatabase())
                && FactoryConst.db.ORACLE.equals(project.getDatabase())
                && !FactoryConst.web.THYMELEAF.equals(project.getWebFramework());
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
