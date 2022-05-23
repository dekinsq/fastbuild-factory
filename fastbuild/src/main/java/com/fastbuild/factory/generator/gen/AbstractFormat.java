package com.fastbuild.factory.generator.gen;

import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.domain.ProjectConfig;
import com.fastbuild.factory.generator.common.FactoryProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFormat implements IGenHandler {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected AppConfig app;

    protected ProjectConfig project;

    protected FactoryProperties properties;

    public AbstractFormat(AppConfig app) {
        this.app = app;
        this.project = app.getProjectConfig();
        this.properties = this.project.getProperties();
    }

    /**
     * 执行生成动作（执行过记录到执行列表中，不再重复执行）
     * @throws Exception
     */
    @Override
    public void gen() throws Exception {
        if (!project.getGenList().contains(this.getGenId()) && this.validate()) {
            logger.info(app.getAppId() + "开始执行");
            this.dependency();
            this.fileGenerator();
            project.getGenList().add(this.getGenId());
        }
    }

    protected abstract String getGenId();

    protected abstract boolean validate();

    protected abstract void dependency() throws Exception;

    protected abstract void fileGenerator() throws Exception;

}
