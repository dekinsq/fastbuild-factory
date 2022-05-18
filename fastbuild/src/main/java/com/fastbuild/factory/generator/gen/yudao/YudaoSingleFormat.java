package com.fastbuild.factory.generator.gen.yudao;

import com.fastbuild.factory.generator.common.FactoryConst;
import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
        String srcPath = properties.getFactoryYudaoVuePath();
        File root = new File(project.getServerRootPath());
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
        if (!project.getPackagePrefix().equals("cn.iocoder.yudao")) {

            File[] modules = root.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory();
                }
            });
            for (File module : modules) {
                File[] children = module.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.isDirectory();
                    }
                });
                for (File child : children) {
                    if (child.getName().contains("yudao-")) {
                        File dest = new File(child.getPath().replace(child.getName(), child.getName().replace("yudao-", project.getProjectName() + "-")));
                        child.renameTo(dest);

                        this.movePackage(dest.getPath());
                    }
                }
                if (module.getName().contains("yudao-")) {
                    File dest = new File(module.getPath().replace(module.getName(), module.getName().replace("yudao-", project.getProjectName() + "-")));
                    module.renameTo(dest);

                    this.movePackage(dest.getPath());
                }
            }

        }
    }

    private void movePackage(String path) throws IOException {
        String packagePath = path + "/src/main/java/cn/iocoder/yudao";
        File packageDir = new File(packagePath);
        if (packageDir.exists()) {
            File destPackage = new File(path + "/src/main/java/" + project.getPackagePrefix().replaceAll("\\.", "/"));
            File[] listFiles = packageDir.listFiles();
            for (File srcFile : listFiles) {
                FileUtils.moveToDirectory(srcFile, destPackage, true);
            }
            if (destPackage.getPath().contains(packageDir.getParent())) {
                FileUtils.deleteDirectory(new File(packagePath));
            } else if (destPackage.getPath().contains(packageDir.getParentFile().getParent())) {
                FileUtils.deleteDirectory(new File(path + "/src/main/java/cn/iocoder"));
            } else if (destPackage.getPath().contains(packageDir.getParentFile().getParentFile().getParent())) {
                FileUtils.deleteDirectory(new File(path + "/src/main/java/cn"));
            }
        }
    }

}
