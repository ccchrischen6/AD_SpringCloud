package com.chrischen.ad.mysql.listener;

import com.chrischen.ad.dto.BinlogRowData;

/**
 * Created by Chris Chen
 */
public interface Ilistener {
    //register different listener according to operation type
    void register();

    void onEvent(BinlogRowData eventData);
}
