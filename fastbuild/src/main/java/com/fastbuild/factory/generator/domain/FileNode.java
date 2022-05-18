package com.fastbuild.factory.generator.domain;

import lombok.Data;

import java.util.List;

@Data
public class FileNode {

    private String key;

    private String title;

    private Boolean isLeaf;

    private Boolean selectable;

    private List<FileNode> children;

}
