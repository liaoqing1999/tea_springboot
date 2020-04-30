package com.qing.tea.utils;

import lombok.Data;

import java.util.List;
@Data
public class Node {
    private String id;

    private String label;

    private String value;

    private Object data;

    private List<Object> children;
}
