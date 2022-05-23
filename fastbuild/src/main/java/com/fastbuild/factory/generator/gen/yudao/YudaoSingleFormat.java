package com.fastbuild.factory.generator.gen.yudao;

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

public class YudaoSingleFormat extends AbstractFormat {

    private final String GEN_ID = "yudao#single";

    public YudaoSingleFormat(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return FactoryConst.app.YUDAO.equals(app.getAppId()) && FactoryConst.server.SINGLE.equals(project.getServerMode());
    }

    @Override
    protected void dependency() {}

    @Override
    protected void fileGenerator() throws Exception {
        String srcPath = properties.getFactoryYudaoVuePath();
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
        javaFormatter.replaceAll("cn.iocoder.yudao", project.getPackagePrefix());
        javaFormatter.replaceAll("YuDao", classNamePrefix);
        javaFormatter.replaceAll("Yudao", classNamePrefix);
        javaFormatter.replaceAll("yudao", varNamePrefix);
        javaFormatter.replaceAll("芋道管理系统", project.getProjectTitle());
        javaFormatter.format();

        FileFormatter xmlFormatter = new FileFormatter(destRoot, FileFilterUtils.suffixFileFilter("xml"));
        xmlFormatter.replaceAll("https://github.com/YunaiV/ruoyi-vue-pro", "http://fastbuild.run");
        xmlFormatter.replaceAll("cn.iocoder.yudao", project.getPackagePrefix());
        xmlFormatter.replaceAll("yudao", varNamePrefix);
        xmlFormatter.replaceAll("芋道项目基础脚手架", project.getProjectTitle());
        xmlFormatter.format();

        FileFormatter ymlFormatter = new FileFormatter(destRoot, FileFilterUtils.suffixFileFilter("yaml"));
        if (FactoryConst.db.ORACLE.equals(project.getDatabase())) {
            ymlFormatter.deleteLine("jdbc:mysql://127.0.0.1:3306");
            ymlFormatter.replace("#          url: jdbc:oracle:thin:@127.0.0.1:1521:xe", "           url: jdbc:oracle:thin:@127.0.0.1:1521:${spring.datasource.dynamic.datasource.master.name}");
        } else if (FactoryConst.db.SQL_SERVER.equals(project.getDatabase())) {
            ymlFormatter.deleteLine("jdbc:mysql://127.0.0.1:3306");
            ymlFormatter.replace("#          url: jdbc:sqlserver://127.0.0.1:1433;DatabaseName=\\\\\\${spring.datasource.dynamic.datasource.master.name}", "           url: jdbc:sqlserver://127.0.0.1:1433;DatabaseName=${spring.datasource.dynamic.datasource.master.name}");
            ymlFormatter.replace("#          url: jdbc:sqlserver://127.0.0.1:1433;DatabaseName=\\\\\\${spring.datasource.dynamic.datasource.slave.name}", "           url: jdbc:sqlserver://127.0.0.1:1433;DatabaseName=${spring.datasource.dynamic.datasource.slave.name}");
        }
        ymlFormatter.replaceAll("yudao", project.getProjectName());
        ymlFormatter.replaceAll("yudao-server", project.getProjectName());
        ymlFormatter.format();

        FileFormatter vmFormatter = new FileFormatter(destRoot, FileFilterUtils.suffixFileFilter("vm"));
        vmFormatter.replaceAll("cn.iocoder.yudao", project.getPackagePrefix());
        vmFormatter.replaceAll("yudao", varNamePrefix);
        vmFormatter.format();

        FileFormatter sqlFormatter = new FileFormatter(destRoot, FileFilterUtils.suffixFileFilter("sql"));
        sqlFormatter.replaceAll("cn.iocoder.yudao", project.getPackagePrefix());
        sqlFormatter.format();

        FileFormatter txtFormatter = new FileFormatter(destRoot, FileFilterUtils.suffixFileFilter("md"));
        txtFormatter.replaceAll("ruoyi", project.getProjectName());
        txtFormatter.format();

        FileFormatter factoriesFormatter = new FileFormatter(destRoot, FileFilterUtils.suffixFileFilter("factories"));
        factoriesFormatter.replaceAll("cn.iocoder.yudao", project.getPackagePrefix());
        factoriesFormatter.replaceAll("YuDao", classNamePrefix);
        factoriesFormatter.replaceAll("Yudao", classNamePrefix);
        factoriesFormatter.format();
    }

    private List<String> getExcludeFile () {
        List<String> exclude = new ArrayList<>();
        exclude.add(".git");
        exclude.add(".github");
        exclude.add(".idea");
        exclude.add("target");
        exclude.add("test");
        exclude.add("admin-ui");
        exclude.add("yudao-ui-admin");
        exclude.add("yudao-ui-app-tmp");
        exclude.add("yudao-ui-app-v1");
        return exclude;
    }

    private Map<String, String> getReplaceDirMap () {
        Map<String, String> replaceDirMap = new LinkedHashMap<>();
        replaceDirMap.put("yudao-", project.getProjectName() + "-");
        String[] packageArr = project.getPackagePrefix().split(".");
        replaceDirMap.put("src/main/java/cn/iocoder/yudao", "src/main/java/" + project.getPackagePrefix().replaceAll("\\.", "/"));
        return replaceDirMap;
    }

    private Map<String, String> getReplaceFileMap () {
        final String classNamePrefix = CaseUtils.toCamelCase(project.getProjectName(), true, new char[] { '-', '_' });
        Map<String, String> replaceFileMap = new LinkedHashMap<>();
        replaceFileMap.put("YuDao", classNamePrefix);
        replaceFileMap.put("Yudao", classNamePrefix);
        return replaceFileMap;
    }

}
