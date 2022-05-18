package com.fastbuild.factory.generator.service;

import com.fastbuild.factory.common.Base64;
import com.fastbuild.factory.generator.domain.FactoryProperties;
import com.fastbuild.factory.generator.domain.FileContent;
import com.fastbuild.factory.generator.domain.FileNode;
import com.fastbuild.factory.generator.domain.ProjectConfig;
import com.fastbuild.factory.generator.gen.GenBuilder;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 处理代码生成业务
 *
 * @author fastbuild@163.com
 */
@Service
public class GenService {

    @Autowired
    private FactoryProperties properties;

    /**
     * 生成项目文件
     * @param config
     * @throws Exception
     */
    public ProjectConfig create(ProjectConfig config) throws Exception {
        String appId = this.getProjectId(config);
        String workPath = this.getWorkPath(appId);
        config.setProjectId(appId);
        config.setWorkPath(workPath);

        // 已存在则不重复生成
        if (new File(workPath).exists()) return config;

        config.setProperties(properties);
        GenBuilder builder = new GenBuilder(config);
        builder.execute();
        return config;
    }

    /**
     * 获取文件树
     * @param config
     * @return
     */
    public List<FileNode> getFileTree(ProjectConfig config) {
        File root = new File(config.getWorkPath());
        List<FileNode> node = new ArrayList<>();
        recursive(root, node);
        return node;
    }

    /**
     * 获取文件内容
     * @param id
     * @return
     * @throws IOException
     */
    public FileContent getFile(String id) throws IOException {
        String path = new String(Base64.decode(id));
        File file = new File(path);
        if (file.isFile() && file.exists()) {
            String name = file.getName();
            FileContent content = new FileContent();
            content.setId(id);
            content.setSuffix(name.substring(name.lastIndexOf(".") + 1));

            List<String> lines = FileUtils.readLines(file);
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                sb.append(line).append("\r\n");
            }
            content.setContent(sb.toString());

            return content;
        }
        return null;
    }

    /**
     * 下载压缩文件
     * @param config
     * @throws Exception
     */
    public File download(ProjectConfig config) throws Exception {
        File zipFile = new File(config.getWorkPath() + ".zip");
        if (!zipFile.exists()) {
            FileOutputStream output = new FileOutputStream(zipFile);
            ZipOutputStream zip = new ZipOutputStream(output);
            this.compress(new File(config.getWorkPath()), zip, config.getProjectId());
            zip.close();
            output.close();
        }
        return zipFile;
    }

    /**
     * 获取项目id
     * @param config
     * @return
     */
    private String getProjectId(ProjectConfig config) {
        return config == null ? null : DigestUtils.md5DigestAsHex(config.toString().getBytes());
    }

    /**
     * 获取工作目录地址
     * @param appId
     * @return
     */
    private String getWorkPath(String appId) {
        String pathTemplate = properties.getFactoryTmpRoot();
        Date date = new Date();
        return String.format(pathTemplate, date, date, date, appId);
    }

    /**
     * 递归生成文件树
     * @param path
     * @param node
     */
    private void recursive(File path, List<FileNode> node) {
        if (path.isDirectory()) {
            File[] list = path.listFiles();
            for (File file : list) {
                FileNode fileNode = new FileNode();
                fileNode.setKey(Base64.encode(file.getAbsolutePath().getBytes()));
                fileNode.setTitle(file.getName());
                node.add(fileNode);
                if (file.isDirectory()) {
                    fileNode.setIsLeaf(false);
                    fileNode.setSelectable(false);
                    List<FileNode> children = new ArrayList<>();
                    fileNode.setChildren(children);
                    recursive(file, children);
                } else {
                    fileNode.setIsLeaf(true);
                    fileNode.setSelectable(true);
                }
            }
        } else {
            FileNode fileNode = new FileNode();
            fileNode.setKey(Base64.encode(path.getAbsolutePath().getBytes()));
            fileNode.setTitle(path.getName());
            fileNode.setIsLeaf(true);
            fileNode.setSelectable(true);
            node.add(fileNode);
        }
    }

    /**
     * 递归压缩文件
     * @param src
     * @param zip
     */
    private void compress(File src, ZipOutputStream zip, String path) throws IOException {
        if (src.isDirectory()) {
            File[] list = src.listFiles();
            if (list == null || list.length == 0) {
                zip.putNextEntry(new ZipEntry(path + File.separator));
            } else {
                for (File file : list) {
                    this.compress(file, zip, path + File.separator + file.getName());
                }
            }
        } else {
            zip.putNextEntry(new ZipEntry(path));
            FileInputStream fos = new FileInputStream(src);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int tag;
            while ((tag = bis.read()) != -1) {
                zip.write(tag);
            }
            bis.close();
            fos.close();
        }
    }

}
