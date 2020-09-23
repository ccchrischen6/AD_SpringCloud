package com.chrischen.ad.handler;

import com.chrischen.ad.index.IndexAware;
import com.chrischen.ad.mysql.constant.OpType;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Chris Chen
 *
 * add, update or delete index
 *
 */

@Slf4j
public class AdLevelDataHandler {
    private static <K,V> void handleBinLogEvent(
            IndexAware<K,V> indexAware,
            K key,
            V value,
            OpType type) {
                switch (type){
                    case ADD:
                        indexAware.add(key, value);
                        break;
                    case UPDATE:
                        indexAware.update(key, value);
                        break;
                    case DELETE:
                        indexAware.delete(key, value);
                        break;
                    default:
                        break;
                }
    }
}
