package com.fastbuild.factory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动程序
 *
 * @author fastbuild@163.com
 */
@SpringBootApplication
public class FastbuildApplication {
    public static void main(String[] args) {
        SpringApplication.run(FastbuildApplication.class, args);
        System.out.println("Fastbuild Factory 启动成功!!");
    }
}
