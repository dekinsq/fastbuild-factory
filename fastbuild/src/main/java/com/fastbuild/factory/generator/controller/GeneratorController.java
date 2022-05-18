package com.fastbuild.factory.generator.controller;

import com.fastbuild.factory.common.AjaxResult;
import com.fastbuild.factory.generator.domain.FileContent;
import com.fastbuild.factory.generator.domain.FileNode;
import com.fastbuild.factory.generator.domain.ProjectConfig;
import com.fastbuild.factory.generator.service.GenService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.List;

/**
 * 处理代码生成业务
 *
 * @author fastbuild@163.com
 */
@RestController
@RequestMapping("/factory/generator")
public class GeneratorController {

    @Autowired
    private GenService genService;

    /**
     * 根据配置项创建项目代码
     *
     * @param request
     * @param config
     * @return
     */
    @PostMapping("/create")
    public AjaxResult create(HttpServletRequest request, @RequestBody ProjectConfig config) {
        try {
            config.setSessionId(request.getSession().getId());
            config.setProjectId(null);
            config.setWorkPath(null);
            config.setProperties(null);
            genService.create(config);
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 根据配置项返回代码目录树
     *
     * @param request
     * @param config
     * @return
     */
    @PostMapping("/file-tree")
    public AjaxResult fileTree(HttpServletRequest request, @RequestBody ProjectConfig config) {
        try {
            config.setSessionId(request.getSession().getId());
            config = genService.create(config);
            List<FileNode> node = genService.getFileTree(config);
            return AjaxResult.success(node);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 根据文件id获取文件内容
     *
     * @param id
     * @return
     */
    @GetMapping("/file")
    public AjaxResult file(@RequestParam String id) {
        try {
            FileContent content = genService.getFile(URLDecoder.decode(id, "utf-8"));
            return AjaxResult.success(content);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 打包下载已生成的文件
     * @param request
     * @param response
     * @param config
     */
    @PostMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response, @RequestBody ProjectConfig config) {
        try {
            config.setSessionId(request.getSession().getId());
            config = genService.create(config);

            File file = genService.download(config);
            byte[] data = FileUtils.readFileToByteArray(file);

            OutputStream output = response.getOutputStream();
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
            response.addHeader("Content-Length", String.valueOf(file.length()));
            response.setContentType("application/octet-stream");
            output.write(data);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
