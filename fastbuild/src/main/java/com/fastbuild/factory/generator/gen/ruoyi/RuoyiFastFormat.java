package com.fastbuild.factory.generator.gen.ruoyi;

import com.fastbuild.factory.generator.common.FactoryConst;
import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RuoyiFastFormat extends AbstractFormat {

    private final String GEN_ID = "ruoyi#fast";

    public RuoyiFastFormat(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return FactoryConst.app.RUOYI.equals(app.getAppId()) && FactoryConst.web.THYMELEAF.equals(project.getWebFramework());
    }

    @Override
    protected void dependency() {}

    @Override
    protected void fileGenerator() throws Exception {
        File root = this.createServerDirectory();
        this.renameServerDirectory(root);
    }

    private File createServerDirectory() throws IOException {
        String srcPath = properties.getFactoryRuoyiFastPath();
        File root = new File(project.getServerRootPath());
        List<String> exclude = new ArrayList<>();
        exclude.add(".git");
        exclude.add(".github");
        exclude.add(".idea");
        FileUtils.copyDirectory(new File(srcPath), root, new FileFileFilter() {
            @Override
            public boolean accept(File file) {
                String fileName = file.getName();
                return !exclude.contains(fileName) && !fileName.endsWith("iml");
            }
        });
        return root;
    }

    private void renameServerDirectory(File root) throws IOException {
        if (!project.getPackagePrefix().equals("com.ruoyi")) {
            // 修改目录
            File[] dirList = root.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory();
                }
            });
            boolean isOracle = FactoryConst.db.ORACLE.equals(this.project.getDatabase());
            String packagePath = isOracle ? "/main/java/com/ruoyi" : "/src/main/java/com/ruoyi";
            String targetPath = (isOracle ? "/main/java/" : "/src/main/java/") + project.getPackagePrefix().replaceAll("\\.", "/");
            for (File dir : dirList) {
                File packageDir = new File(dir.getPath() + packagePath);
                if (packageDir.exists()) {
                    File targetPackage = new File(dir.getPath() + targetPath);
                    File[] listFiles = packageDir.listFiles();
                    for (File srcFile : listFiles) {
                        FileUtils.moveToDirectory(srcFile, targetPackage, true);
                    }
                    FileUtils.deleteDirectory(packageDir);
                }
                dir.renameTo(new File(dir.getPath().replace("ruoyi-", project.getProjectName() + "-")));
            }
        }
    }


}
