package com.fastbuild.factory.generator.common;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFileFilter;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 文件帮助类
 *
 * @author fastbuild@163.com
 */
public class FileHelper {

    /**
     * 递归复制目录及目录下的文件
     *
     * @param srcDir 源目录
     * @param destDir 目标目录
     * @param filter 过滤器
     * @param replaceDirMap 需要替换的目录
     * @param replaceFileMap 需要替换的文件名
     * @throws IOException
     */
    public static void copyDirectory(File srcDir, File destDir, FileFilter filter,
                                     Map<String, String> replaceDirMap, Map<String, String> replaceFileMap) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null");
        } else if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!srcDir.exists()) {
            throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
        } else if (!srcDir.isDirectory()) {
            throw new IOException("Source '" + srcDir + "' exists but is not a directory");
        } else if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
            throw new IOException("Source '" + srcDir + "' and destination '" + destDir + "' are the same");
        } else {
            List<String> exclusionList = null;
            if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
                File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
                if (srcFiles != null && srcFiles.length > 0) {
                    exclusionList = new ArrayList(srcFiles.length);
                    File[] arr$ = srcFiles;
                    int len$ = srcFiles.length;

                    for(int i$ = 0; i$ < len$; ++i$) {
                        File srcFile = arr$[i$];
                        File copiedFile = new File(destDir, srcFile.getName());
                        exclusionList.add(copiedFile.getCanonicalPath());
                    }
                }
            }

            doCopyDirectory(srcDir, destDir, filter, exclusionList, replaceDirMap, replaceFileMap);
        }
    }

    private static void doCopyDirectory(File srcDir, File destDir, FileFilter filter, List<String> exclusionList,
                                        Map<String, String> replaceDirMap, Map<String, String> replaceFileMap) throws IOException {
        String destPath = destDir.getPath().replaceAll("\\\\", "/");
        String replacedDestPath = replaceMap(destPath, replaceDirMap, true);
        destDir = new File(replacedDestPath);

        File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
        if (srcFiles == null) {
            throw new IOException("Failed to list contents of " + srcDir);
        } else {
            if (destDir.exists()) {
                if (!destDir.isDirectory()) {
                    throw new IOException("Destination '" + destDir + "' exists but is not a directory");
                }
            } else if (!destDir.mkdirs() && !destDir.isDirectory()) {
                throw new IOException("Destination '" + destDir + "' directory cannot be created");
            }

            if (!destDir.canWrite()) {
                throw new IOException("Destination '" + destDir + "' cannot be written to");
            } else {
                File[] arr$ = srcFiles;
                int len$ = srcFiles.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    File srcFile = arr$[i$];
                    String fileName = srcFile.getName();
                    if (srcFile.isFile()) {
                        fileName = replaceMap(fileName, replaceFileMap, false);
                    }
                    File dstFile = new File(destDir, fileName);
                    if (exclusionList == null || !exclusionList.contains(srcFile.getCanonicalPath())) {
                        if (srcFile.isDirectory()) {
                            doCopyDirectory(srcFile, dstFile, filter, exclusionList, replaceDirMap, replaceFileMap);
                        } else {
                            doCopyFile(srcFile, dstFile);
                        }
                    }
                }

                destDir.setLastModified(srcDir.lastModified());
            }
        }
    }

    private static void doCopyFile(File srcFile, File destFile) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        } else {
            FileInputStream fis = null;
            FileOutputStream fos = null;
            FileChannel input = null;
            FileChannel output = null;

            try {
                fis = new FileInputStream(srcFile);
                fos = new FileOutputStream(destFile);
                input = fis.getChannel();
                output = fos.getChannel();
                long size = input.size();
                long pos = 0L;

                for(long count = 0L; pos < size; pos += output.transferFrom(input, pos, count)) {
                    count = size - pos > 31457280L ? 31457280L : size - pos;
                }
            } finally {
                IOUtils.closeQuietly(output);
                IOUtils.closeQuietly(fos);
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(fis);
            }

            if (srcFile.length() != destFile.length()) {
                throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
            } else {
                destFile.setLastModified(srcFile.lastModified());
            }
        }
    }

    private static String replaceMap(String str, Map<String, String> map, boolean isAll) {
        if (str != null && map != null) {
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                if (isAll) {
                    str = str.replaceAll(key, map.get(key));
                } else {
                    str = str.replace(key, map.get(key));
                }
            }
        }
        return str;
    }

}
