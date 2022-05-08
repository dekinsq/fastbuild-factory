package com.fastbuild.factory.generator.gen;

import com.fastbuild.factory.generator.common.FactoryConst;
import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.domain.ProjectConfig;
import com.fastbuild.factory.generator.gen.ruoyi.RuoyiHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GenBuilder {

    private ProjectConfig config;

    private GenBuilder() {}

    public GenBuilder(ProjectConfig config) {
        this.config = config;
    }

    /**
     * 执行生成
     * @throws Exception
     */
    public void execute() throws Exception {
        this.createRoot();

        List<AppConfig> appList = config.getAppList();
        if (appList == null || appList.size() == 0) {
            appList = new ArrayList<>();
            config.setAppList(appList);
        }

        for (AppConfig app : appList) {
            app.setProjectConfig(config);
            IGenHandler handler = this.getGenHandler(app);
            if (handler != null) {
                handler.gen();
            }
        };
    }

    private IGenHandler getGenHandler(AppConfig app) {
        switch (app.getAppId()) {
            case FactoryConst.app.RUOYI: {
                return new RuoyiHandler(app);
            }
        }
        return null;
    }

    private void createRoot() {
        File root = new File(config.getWorkPath());
        if (root.exists()) {
            return;
        } else {
            root.mkdirs();
        }
    }

}
