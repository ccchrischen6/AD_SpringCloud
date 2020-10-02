package com.chrischen.ad.mysql.listener;

import com.chrischen.ad.mysql.dto.BinlogRowData;

/**
 * Created by Chris Chen
 */
public interface Ilistener {
    //register different listener according to opertion type
    void register();

    void onEvent(BinlogRowData eventData);
}
