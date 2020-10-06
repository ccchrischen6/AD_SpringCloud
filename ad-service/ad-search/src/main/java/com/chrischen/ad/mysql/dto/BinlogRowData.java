package com.chrischen.ad.mysql.dto;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by Chris Chen
 * convert event to object
 */

@Data
public class BinlogRowData {

    private TableTemplate table;
    private EventType eventType;

    //map: columnName -> the value of operation
    private List<Map<String, String>> before;
    private List<Map<String, String>> after;
}
