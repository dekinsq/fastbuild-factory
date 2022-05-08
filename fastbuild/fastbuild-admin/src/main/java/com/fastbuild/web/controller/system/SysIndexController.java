package com.fastbuild.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fastbuild.common.config.FastbuildConfig;
import com.fastbuild.common.utils.StringUtils;

/**
 * 首页
 *
 * @author fastbuild@163.com
 */
@RestController
public class SysIndexController
{
    /** 系统基础配置 */
    @Autowired
    private FastbuildConfig fastbuildConfig;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index()
    {
        return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", fastbuildConfig.getName(), fastbuildConfig.getVersion());
    }
}
