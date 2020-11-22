package com.chrischen.ad.dto;

import com.chrischen.ad.constant.OpType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Chris Chen
 */

@Data
public class ParseTemplate {

    private String databases;

    // tableName -> tableTemplate
    private Map<String, TableTemplate> tableTemplateMap = new HashMap<>();

    public static ParseTemplate parse(Template _template){
        ParseTemplate template = new ParseTemplate();
        template.setDatabases(_template.getDatabase());

        for (JsonTable table : _template.getTableList()) {
            String name = table.getTableName();
            Integer level = table.getLevel();

            TableTemplate tableTemplate = new TableTemplate();
            tableTemplate.setTableName(name);
            tableTemplate.setLevel(level.toString());
            template.tableTemplateMap.put(name, tableTemplate);

            //traverse all the columns
            Map<OpType, List<String>> opTypeListMap =
                    tableTemplate.getOpTypeFieldSetMap();

            for (JsonTable.Column column : table.getInsert()) {
                getAndCreateIfNed(
                        OpType.ADD,
                        opTypeListMap,
                        ArrayList::new
                ).add(column.getColumn());
            }

            for (JsonTable.Column column : table.getUpdate()) {
                getAndCreateIfNed(
                        OpType.UPDATE,
                        opTypeListMap,
                        ArrayList::new
                ).add(column.getColumn());
            }

            for (JsonTable.Column column : table.getDelete()) {
                getAndCreateIfNed(
                        OpType.DELETE,
                        opTypeListMap,
                        ArrayList::new
                ).add(column.getColumn());
            }
        }

        return template;
    }

    private static <T, R> R getAndCreateIfNed(T key, Map<T, R> map,
                                              Supplier<R> factory) {
        return map.computeIfAbsent(key, k -> factory.get());
    }



}
