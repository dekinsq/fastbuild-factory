package com.fastbuild.factory.generator.domain;

import lombok.Data;

@Data
public class AppConfig {

    private String appId;

    private String appType;

    private ProjectConfig projectConfig;

}
