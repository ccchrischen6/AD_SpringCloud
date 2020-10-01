package com.chrischen.ad.mysql;

import com.alibaba.fastjson.JSON;
import com.chrischen.ad.mysql.constant.OpType;
import com.chrischen.ad.mysql.dto.ParseTemplate;
import com.chrischen.ad.mysql.dto.TableTemplate;
import com.chrischen.ad.mysql.dto.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris Chen
 */

@Slf4j
@Component
public class TemplateHolder {
    private ParseTemplate template;
    private final JdbcTemplate jdbcTemplate;

    private String SQL_SCHEMA = "select table_schema, table_name, " +
            "column_name, ordinal_position from information_schema.columns " +
            "where table_schema = ? and table_name = ?";

    @Autowired
    public TemplateHolder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void init(){
        loadJson("template.json");
    }

    public TableTemplate getTable(String tableName) {
        return template.getTableTemplateMap().get(tableName);
    }

    private void loadJson(String path) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = cl.getResourceAsStream(path);

        try {
            Template template = JSON.parseObject(
                    inputStream,
                    Charset.defaultCharset(),
                    Template.class
            );

            this.template = ParseTemplate.parse(template);
            loadMeta();
        }
        catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("failed to parse json file");
        }
    }


    // link index to column name
    private void loadMeta() {
        for (Map.Entry<String, TableTemplate> entry : template.getTableTemplateMap().entrySet()) {
            TableTemplate table = entry.getValue();

            List<String> updateFields = table.getOpTypeFieldSetMap().get(
                    OpType.UPDATE
            );

            List<String> insertFields = table.getOpTypeFieldSetMap().get(
                    OpType.ADD
            );

            List<String> deleteFields = table.getOpTypeFieldSetMap().get(
                    OpType.DELETE
            );

            jdbcTemplate.query(SQL_SCHEMA, new Object[]{
                    template.getDatabases(), table.getTableName()
            }, (rs, i) -> {

                int pos = rs.getInt("ORDINAL_POSITION");
                String colName = rs.getString("COLUMN_NAME");

                if ((null != updateFields && updateFields.contains(colName))
                        || (null != insertFields && insertFields.contains(colName))
                        || (null != deleteFields && deleteFields.contains(colName))) {
                    table.getPosMap().put(pos - 1, colName);
                }

                return null;
            });
        }
    }
}
