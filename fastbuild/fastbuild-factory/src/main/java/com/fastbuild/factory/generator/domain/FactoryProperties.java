package com.fastbuild.factory.generator.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {
        "classpath:factory.properties",
}, encoding = "utf-8")
@Data
public class FactoryProperties {

    @Value("${factory.tmp.root}")
    private String factoryTmpRoot;

    /** ruoyi applications */
    @Value("${factory.ruoyi.vue.path}")
    private String factoryRuoyiVuePath;

    @Value("${factory.ruoyi.vue.antd.path}")
    private String factoryRuoyiVueAntdPath;

    @Value("${factory.ruoyi.vue3.path}")
    private String factoryRuoyiVue3Path;

    @Value("${factory.ruoyi.vue3.antd.path}")
    private String factoryRuoyiVue3AntdPath;

    @Value("${factory.ruoyi.oracle.path}")
    private String factoryRuoyiOraclePath;

    @Value("${factory.ruoyi.sqlserver.path}")
    private String factoryRuoyiSqlServerPath;
    /** ruoyi applications end */

}
