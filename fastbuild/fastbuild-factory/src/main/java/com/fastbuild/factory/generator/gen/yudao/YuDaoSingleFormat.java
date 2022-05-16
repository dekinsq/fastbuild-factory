package com.fastbuild.factory.generator.gen.yudao;

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

public class YuDaoSingleFormat extends AbstractFormat {

    private final String GEN_ID = "yudao#single";

    public YuDaoSingleFormat(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return FactoryConst.app.YUDAO.equals(app.getAppId());
    }

    @Override
    protected void dependency() {}

    @Override
    protected void fileGenerator() throws Exception {
        File root = this.createServerDirectory();
        this.renameServerDirectory(root);
    }

    private File createServerDirectory() throws IOException {
        String srcPath = null;
        if (FactoryConst.db.MYSQL.equals(this.project.getDatabase())) {
            srcPath = properties.getFactoryRuoyiVuePath();
        } else if (FactoryConst.db.ORACLE.equals(this.project.getDatabase())) {
            srcPath = properties.getFactoryRuoyiOraclePath();
        } else if (FactoryConst.db.SQL_SERVER.equals(this.project.getDatabase())) {
            srcPath = properties.getFactoryRuoyiSqlServerPath();
        }
        File root = new File(project.getServerRootPath());
        List<String> exclude = new ArrayList<>();
        exclude.add(".git");
        exclude.add(".github");
        exclude.add(".idea");
        exclude.add("bin");
        exclude.add("doc");
        exclude.add("ruoyi-ui");
        exclude.add("LICENSE");
        exclude.add("README.md");
        exclude.add("ry.bat");
        exclude.add("ry.sh");
        exclude.add("v3");
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
