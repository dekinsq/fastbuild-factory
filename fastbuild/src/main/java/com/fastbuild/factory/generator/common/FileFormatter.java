package com.fastbuild.factory.generator.common;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * pom.xml文件处理
 *
 * @author fastbuild@163.com
 */
public class FileFormatter {

    private String encoding = "utf-8";

    private Map<String, String> replaceMap = new LinkedHashMap<>();

    private Map<String, String> replaceAllMap = new LinkedHashMap<>();

    private List<String> deleteLineList = new ArrayList<>();

    private File pathName;

    private IOFileFilter ioFileFilter;

    public FileFormatter(File pathName, IOFileFilter ioFileFilter) {
        this.pathName = pathName;
        this.ioFileFilter = ioFileFilter;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Iterator<File> iteratePomFile () {
        return FileUtils.iterateFiles(this.pathName, ioFileFilter, DirectoryFileFilter.INSTANCE);
    }

    public void format () throws IOException {
        Iterator<File> fileIterator = iteratePomFile();
        Set<String> replaceKeys = replaceMap.keySet();
        Set<String> replaceAllKeys = replaceAllMap.keySet();
        while (fileIterator.hasNext()) {
            File file = fileIterator.next();
            Iterator<String> lineIterator = FileUtils.lineIterator(file, this.encoding);

            List<String> contentList = new ArrayList<>();
            while (lineIterator.hasNext()) {
                String line = lineIterator.next();
                if (!deleteLineList.contains(line.trim())) {
                    for (String key : replaceKeys) {
                        line = line.replace(key, replaceMap.get(key));
                    }
                    for (String key : replaceAllKeys) {
                        line = line.replaceAll(key, replaceAllMap.get(key));
                    }
                    contentList.add(line);
                }
            }

            FileUtils.writeLines(file, this.encoding, contentList);
        }
    }

    public FileFormatter replaceAll(String regx, String replacement) {
        replaceAllMap.put(regx, replacement);
        return this;
    }

    public FileFormatter replace(String regx, String replacement) {
        replaceMap.put(regx, replacement);
        return this;
    }

    public FileFormatter deleteLine(String line) {
        deleteLineList.add(line);
        return this;
    }

}
