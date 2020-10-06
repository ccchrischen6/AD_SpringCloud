package com.chrischen.ad.mysql.listener;

import com.chrischen.ad.mysql.TemplateHolder;
import com.chrischen.ad.mysql.dto.BinlogRowData;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chris Chen
 */

@Slf4j
@Component
public class AggregationListener implements BinaryLogClient.EventListener {

    private String dbName;
    private String tableName;

    private Map<String, Ilistener> listenerMap = new HashMap<>();
    private final TemplateHolder templateHolder;

    @Autowired
    public AggregationListener(TemplateHolder templateHolder) {
        this.templateHolder = templateHolder;
    }

    private String genKey(String dbName, String tableName) {
        return dbName + ":" + tableName;
    }

    public void register(String _dbName, String _tableName,
                         Ilistener ilistener) {
        log.info("register : {}-{}", _dbName, _tableName);

        this.listenerMap.put(genKey(_dbName, _tableName), ilistener);
    }

    @Override
    public void onEvent(Event event) {
        EventType type = event.getHeader().getEventType();
        log.debug("event type: {}", type);

        //check if map fulfillment is needed
        if (type == EventType.TABLE_MAP) {
            TableMapEventData data = event.getData();
            this.tableName = data.getTable();
            this.dbName = data.getDatabase();
            return;
        }

        //if the execution is not ether update, write, or delete,
        //jump out
        if (type != EventType.EXT_UPDATE_ROWS &&
                type != EventType.EXT_WRITE_ROWS &&
                type != EventType.EXT_DELETE_ROWS) {
            return;
        }

        //find
        String key = genKey(this.dbName, this.tableName);
        Ilistener ilistener = listenerMap.get(key);
        if (null == ilistener) {
            log.debug("skip {}", key);
            return;
        }

        log.info("trigger event : {}", type.name());

        try {
            BinlogRowData rowData = buildRowData(event.getData());
            if (rowData == null) {
                return;
            }

            rowData.setEventType(type);
            ilistener.onEvent(rowData);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            this.dbName = "";
            this.tableName = "";
        }
    }

    private BinlogRowData buildRowData(EventData eventData) {
        return null;
    }
}
