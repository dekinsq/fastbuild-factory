package com.fastbuild.factory.generator.common;

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
    @Value("${factory.ruoyi.fast.path}")
    private String factoryRuoyiFastPath;

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

    @Value("${factory.ruoyi.cloud.path}")
    private String factoryRuoyiCloudPath;
    /** ruoyi applications end */

    /** yudao applications */
    @Value("${factory.yudao.vue.path}")
    private String factoryYudaoVuePath;
    /** yudao applications end */

    /** jeecg applications */
    @Value("${factory.jeecg.vue.path}")
    private String factoryJeecgVuePath;
    /** jeecg applications end */

}
