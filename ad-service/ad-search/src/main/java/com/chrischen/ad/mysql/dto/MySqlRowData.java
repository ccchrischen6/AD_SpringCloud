package com.chrischen.ad.mysql.dto;

import com.chrischen.ad.mysql.constant.OpType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris Chen
 */
public class MySqlRowData {
    private String tableName;

    private String level;

    private OpType opType;

    private List<Map<String, String>> fieldValueMap = new ArrayList<>();
}
