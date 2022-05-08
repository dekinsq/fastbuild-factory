package com.fastbuild.factory.generator.service;

import com.fastbuild.common.annotation.Log;
import com.fastbuild.common.core.domain.model.LoginUser;
import com.fastbuild.common.enums.BusinessStatus;
import com.fastbuild.common.utils.SecurityUtils;
import com.fastbuild.common.utils.ServletUtils;
import com.fastbuild.common.utils.StringUtils;
import com.fastbuild.common.utils.ip.IpUtils;
import com.fastbuild.framework.manager.AsyncManager;
import com.fastbuild.framework.manager.factory.AsyncFactory;
import com.fastbuild.system.domain.SysOperLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    public static void handleLog(final Exception e, String method, String operParam)
    {
        try
        {

            // 获取当前的用户
//            LoginUser loginUser = SecurityUtils.getLoginUser();
            LoginUser loginUser = null;

                    // *========数据库日志=========*//
            SysOperLog operLog = new SysOperLog();
            operLog.setOperParam(operParam);
            operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            // 请求的地址
            String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
            operLog.setOperIp(ip);
            operLog.setOperUrl(ServletUtils.getRequest().getRequestURI());
            if (loginUser != null)
            {
                operLog.setOperName(loginUser.getUsername());
            }

            if (e != null)
            {
                operLog.setStatus(BusinessStatus.FAIL.ordinal());
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            operLog.setMethod(method);
            // 设置请求方式
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 保存数据库
            AsyncManager.me().execute(AsyncFactory.recordOper(operLog));
        }
        catch (Exception exp)
        {
            // 记录本地异常日志
            logger.error("==前置通知异常==");
            logger.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

}
