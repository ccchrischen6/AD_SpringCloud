package com.chrischen.ad.service;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;

/**
 * Created by Chris Chen
 *
 * Write---------------
 * WriteRowsEventData{tableId=22, includedColumns={0, 1, 2}, rows=[
 *     [11, 11, BWM]
 * ]}
 *
 * Update--------------
 * UpdateRowsEventData{tableId=22, includedColumnsBeforeUpdate={0, 1, 2}, includedColumns={0, 1, 2}, rows=[
 *     {before=[11, 11, BWM], after=[11, 11, Audi]}
 * ]}
 *
 * Delete--------------
 * DeleteRowsEventData{tableId=22, includedColumns={0, 1, 2}, rows=[
 *     [13, 13, GM]
 * ]}
 *
 *
 *
 * select table_schema, table_name, column_name, ordinal_position from information_schema.columns
 * where table_schema = "ad_data" and table_name = "ad_unit_keyword";
 *
 * +--------------+-----------------+-------------+------------------+
 * | table_schema | table_name      | column_name | ordinal_position |
 * +--------------+-----------------+-------------+------------------+
 * | ad_data      | ad_unit_keyword | id          |                1 |
 * | ad_data      | ad_unit_keyword | unit_id     |                2 |
 * | ad_data      | ad_unit_keyword | keyword     |                3 |
 * +--------------+-----------------+-------------+------------------+
 */
public class BinlogServiceTest {
    public static void main(String[] args) throws Exception{
        BinaryLogClient client = new BinaryLogClient(
                "127.0.0.1",
                3306,
                "root",
                ""
        );

//        client.setBinlogFilename();
//        client.setBinlogPosition();
        client.registerEventListener(event -> {
            EventData data = event.getData();

            if (data instanceof UpdateRowsEventData) {
                System.out.println("Update--------------");
                System.out.println(data.toString());
            } else if (data instanceof WriteRowsEventData) {
                System.out.println("Write---------------");
                System.out.println(data.toString());
            } else if (data instanceof DeleteRowsEventData) {
                System.out.println("Delete--------------");
                System.out.println(data.toString());
            }
        });

        client.connect();

    }
}
