package com.qing.tea.utils;

import lombok.Data;

import java.util.List;
@Data
public class Node {
    private String id;

    private String label;

    private String value;

    private Object data;

    private boolean disabled;

    private boolean isLeaf;

    private boolean checkable;

    private boolean disableCheckbox;

    private List<Node> children;
}
