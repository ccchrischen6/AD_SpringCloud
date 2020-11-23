package com.chrischen.ad.sender;

import com.chrischen.ad.dto.MySqlRowData;

/**
 * Created by Chris Chen
 */
public interface ISender {
    void send(MySqlRowData rowData);
}
