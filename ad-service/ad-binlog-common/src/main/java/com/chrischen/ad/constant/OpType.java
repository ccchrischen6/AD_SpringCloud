package com.chrischen.ad.constant;

import com.github.shyiko.mysql.binlog.event.EventType;

/**
 * Created by Chris Chen
 *
 * operation type
 */
public enum  OpType {
    ADD,
    DELETE,
    UPDATE,
    OTHER;

    public static OpType to (EventType type){
        switch (type) {
            case EXT_UPDATE_ROWS:
                return UPDATE;
            case EXT_WRITE_ROWS:
                return ADD;
            case EXT_DELETE_ROWS:
                return DELETE;
            default:
                return OTHER;
        }
    }
}
