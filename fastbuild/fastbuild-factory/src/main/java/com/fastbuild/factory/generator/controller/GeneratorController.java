package com.fastbuild.factory.generator.controller;

import com.fastbuild.factory.generator.domain.FileContent;
import com.fastbuild.factory.generator.domain.FileNode;
import com.fastbuild.factory.generator.domain.ProjectConfig;
import com.fastbuild.factory.generator.service.GenService;
import com.fastbuild.common.core.controller.BaseController;
import com.fastbuild.common.core.domain.AjaxResult;
import com.fastbuild.factory.generator.service.LogService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/factory/generator")
public class GeneratorController extends BaseController {

    @Autowired
    private GenService genService;

    @PostMapping("/create")
    public AjaxResult create(HttpServletRequest request, @RequestBody ProjectConfig config) {
        try {
            config.setSessionId(request.getSession().getId());
            config.setProjectId(null);
            config.setWorkPath(null);
            config.setProperties(null);
            genService.create(config);
            LogService.handleLog(null, "GeneratorController.create()", config.toString());
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            LogService.handleLog(e, "GeneratorController.create()", config == null ? null : config.toString());
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/file-tree")
    public AjaxResult fileTree(HttpServletRequest request, @RequestBody ProjectConfig config) {
        try {
            config.setSessionId(request.getSession().getId());
            config = genService.create(config);
            List<FileNode> node = genService.getFileTree(config);
            return AjaxResult.success(node);
        } catch (Exception e) {
            e.printStackTrace();
            LogService.handleLog(e, "GeneratorController.fileTree()", config == null ? null : config.toString());
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping("/file")
    public AjaxResult file(@RequestParam String id) {
        try {
            FileContent content = genService.getFile(URLDecoder.decode(id, "utf-8"));
            return AjaxResult.success(content);
        } catch (Exception e) {
            e.printStackTrace();
            LogService.handleLog(e, "GeneratorController.file()", id);
            return AjaxResult.error(e.getMessage());
        }
    }

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
            LogService.handleLog(e, "GeneratorController.download()", config == null ? null : config.toString());
        }
    }

}
