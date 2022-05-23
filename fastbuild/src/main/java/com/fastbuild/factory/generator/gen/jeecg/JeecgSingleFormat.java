package com.fastbuild.factory.generator.gen.jeecg;

import com.fastbuild.factory.generator.common.FactoryConst;
import com.fastbuild.factory.generator.common.FileFormatter;
import com.fastbuild.factory.generator.common.FileHelper;
import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import org.apache.commons.text.CaseUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * jeecg 组件单体项目格式化
 *
 * @author fastbuild@163.com
 */
public class JeecgSingleFormat extends AbstractFormat {

    private final String GEN_ID = "jeecg#single";

    public JeecgSingleFormat(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return FactoryConst.app.JEECG.equals(app.getAppId());
    }

    @Override
    protected void dependency() {}

    @Override
    protected void fileGenerator() throws Exception {
        String srcPath = properties.getFactoryJeecgVuePath() + File.separator + "jeecg-boot";
        File root = new File(project.getServerRootPath());

        List<String> exclude = new ArrayList<>();
        exclude.add(".git");
        exclude.add(".github");
        exclude.add(".idea");
        exclude.add("target");
        exclude.add("test");
        exclude.add("ant-design-vue-jeecg");
//        exclude.add("jeecg-boot-module-demo");
//        exclude.add("jeecg-system-cloud-api");
//        exclude.add("jeecg-boot-starter");
//        exclude.add("jeecg-cloud-module");

        Map<String, String> replaceDirMap = new LinkedHashMap<>();
        replaceDirMap.put("jeecg-boot-", project.getProjectName() + "-");
        replaceDirMap.put("jeecg-system-", project.getProjectName() + "-");
        replaceDirMap.put("src/main/java/org", "src/main/java/com");
        replaceDirMap.put("src/main/java/com/jeecg", "src/main/java/" + project.getPackagePrefix().replaceAll("\\.", "/"));
        replaceDirMap.put("src/main/resources/jeecg", "src/main/resources/" + project.getProjectName());

        final String classNamePrefix = CaseUtils.toCamelCase(project.getProjectName(), true, new char[] { '-', '_' });
        Map<String, String> replaceFileMap = new LinkedHashMap<>();
        replaceDirMap.put("Jeecg", classNamePrefix);
        replaceDirMap.put("JeecgBoot", classNamePrefix);
        replaceDirMap.put("Jeeccg", classNamePrefix);
        FileHelper.copyDirectory(new File(srcPath), root, new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !exclude.contains(file.getName());
            }
        }, replaceDirMap, replaceFileMap);

        FileFormatter fileFormatter = new FileFormatter(root, null);
        fileFormatter.replaceAll("org.jeecgframework.boot", project.getPackagePrefix());
        fileFormatter.replaceAll("JEECG BOOT", project.getProjectTitle());
        fileFormatter.replaceAll("jeecgboot.version", project.getProjectName() + ".version");
        fileFormatter.replaceAll("jeecg-boot-", project.getProjectName() + "-");
        fileFormatter.replaceAll("jeecg-system-", project.getProjectName() + "-");
//        pomHandler.replaceAll("<skip.springboot.maven>false</skip.springboot.maven>", "<skip.springboot.maven>true</skip.springboot.maven>");
//        pomHandler.deleteLine("<module>fastbuild-module-demo</module>");
        fileFormatter.format();
    }

}
