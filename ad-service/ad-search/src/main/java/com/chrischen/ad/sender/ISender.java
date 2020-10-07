package com.chrischen.ad.sender;

import com.chrischen.ad.mysql.dto.MySqlRowData;

/**
 * Created by Chris Chen
 */
public interface ISender {
    void sender(MySqlRowData rowData);
}
