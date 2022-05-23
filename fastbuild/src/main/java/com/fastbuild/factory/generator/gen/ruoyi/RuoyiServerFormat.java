package com.fastbuild.factory.generator.gen.ruoyi;

import com.fastbuild.factory.generator.common.FactoryConst;
import com.fastbuild.factory.generator.common.FileFormatter;
import com.fastbuild.factory.generator.common.FileHelper;
import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.text.CaseUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 若依单体服务端代码格式化
 *
 * @author fastbuild@163.com
 */
public class RuoyiServerFormat extends AbstractFormat {

    private final String GEN_ID = "ruoyi#single";

    public RuoyiServerFormat(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return FactoryConst.app.RUOYI.equals(app.getAppId());
    }

    @Override
    protected void dependency() {}

    @Override
    protected void fileGenerator() throws Exception {
        String srcPath = this.getSrcPath();
        File srcFile = new File(srcPath);
        File destRoot = new File(project.getServerRootPath());

        List<String> exclude = this.getExcludeFile();
        Map<String, String> replaceDirMap = this.getReplaceDirMap();
        Map<String, String> replaceFileMap = this.getReplaceFileMap();

        FileHelper.copyDirectory(srcFile, destRoot, new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !exclude.contains(file.getName());
            }
        }, replaceDirMap, replaceFileMap);

        this.fileContentFormat(destRoot);
    }

    /**
     * 格式化文件内容
     *
     * @param destRoot
     * @throws IOException
     */
    private void fileContentFormat (File destRoot) throws IOException {
        final String classNamePrefix = CaseUtils.toCamelCase(project.getProjectName(), true, new char[] { '-', '_' });
        final String varNamePrefix = CaseUtils.toCamelCase(project.getProjectName(), false, new char[] { '-', '_' });

        FileFormatter javaFormatter = new FileFormatter(destRoot, FileFilterUtils.suffixFileFilter("java"));
        javaFormatter.replaceAll("com.ruoyi", project.getPackagePrefix());
        javaFormatter.replaceAll("RuoYi", classNamePrefix);
        javaFormatter.replaceAll("Ruoyi", classNamePrefix);
        javaFormatter.replaceAll("ruoyi", varNamePrefix);
        javaFormatter.replaceAll("若依", project.getProjectTitle());
        javaFormatter.format();

        FileFormatter xmlFormatter = new FileFormatter(destRoot, FileFilterUtils.suffixFileFilter("xml"));
        xmlFormatter.replaceAll("http://www.ruoyi.vip", "http://fastbuild.run");
        xmlFormatter.replaceAll("com.ruoyi", project.getPackagePrefix());
        xmlFormatter.replaceAll("ruoyi", varNamePrefix);
        xmlFormatter.format();

        FileFormatter ymlFormatter = new FileFormatter(destRoot, FileFilterUtils.suffixFileFilter("yml"));
        ymlFormatter.replaceAll("RuoYi", project.getProjectName());
        ymlFormatter.replaceAll("ruoyi", varNamePrefix);
        ymlFormatter.format();

        FileFormatter vmFormatter = new FileFormatter(destRoot, FileFilterUtils.suffixFileFilter("vm"));
        vmFormatter.replaceAll("com.ruoyi", project.getPackagePrefix());
        vmFormatter.replaceAll("ruoyi", varNamePrefix);
        vmFormatter.format();

        FileFormatter sqlFormatter = new FileFormatter(destRoot, FileFilterUtils.suffixFileFilter("sql"));
        sqlFormatter.replaceAll("ruoyi", project.getProjectName());
        sqlFormatter.format();

        FileFormatter txtFormatter = new FileFormatter(destRoot, FileFilterUtils.suffixFileFilter("txt"));
        txtFormatter.replaceAll("ruoyi", project.getProjectName());
        txtFormatter.format();

        FileFormatter factoriesFormatter = new FileFormatter(destRoot, FileFilterUtils.suffixFileFilter("factories"));
        factoriesFormatter.replaceAll("com.ruoyi", project.getPackagePrefix());
        factoriesFormatter.replaceAll("YuDao", classNamePrefix);
        factoriesFormatter.replaceAll("Yudao", classNamePrefix);
        factoriesFormatter.format();
    }

    private String getSrcPath () {
        if (FactoryConst.server.CLOUD.equals(this.project.getServerMode())) {
            return properties.getFactoryRuoyiCloudPath();
        } else if (FactoryConst.web.THYMELEAF.equals(this.project.getWebFramework())) {
            return properties.getFactoryRuoyiFastPath();
        } else if (FactoryConst.db.MYSQL.equals(this.project.getDatabase())) {
            return properties.getFactoryRuoyiVuePath();
        } else if (FactoryConst.db.ORACLE.equals(this.project.getDatabase())) {
            return properties.getFactoryRuoyiOraclePath();
        } else if (FactoryConst.db.SQL_SERVER.equals(this.project.getDatabase())) {
            return properties.getFactoryRuoyiSqlServerPath();
        }
        return null;
    }

    private List<String> getExcludeFile () {
        List<String> exclude = new ArrayList<>();
        exclude.add(".git");
        exclude.add(".github");
        exclude.add(".idea");
        exclude.add("ruoyi-ui");
        exclude.add("README.md");
        exclude.add("v3");
        return exclude;
    }

    private Map<String, String> getReplaceDirMap () {
        Map<String, String> replaceDirMap = new LinkedHashMap<>();
        replaceDirMap.put("ruoyi-", project.getProjectName() + "-");
        replaceDirMap.put("src/main/java/com/ruoyi", "src/main/java/" + project.getPackagePrefix().replaceAll("\\.", "/"));
        return replaceDirMap;
    }

    private Map<String, String> getReplaceFileMap () {
        final String classNamePrefix = CaseUtils.toCamelCase(project.getProjectName(), true, new char[] { '-', '_' });
        Map<String, String> replaceFileMap = new LinkedHashMap<>();
        replaceFileMap.put("Ruoyi", classNamePrefix);
        replaceFileMap.put("RuoYi", classNamePrefix);
        return replaceFileMap;
    }

}
