package com.chrischen.ad.sender;

import com.alibaba.fastjson.JSON;
import com.chrischen.ad.dump.table.AdPlanTable;
import com.chrischen.ad.index.DataLevel;
import com.chrischen.ad.mysql.constant.Constant;
import com.chrischen.ad.mysql.dto.MySqlRowData;
import com.chrischen.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris Chen
 */

@Slf4j
@Component("indexSender")
public class IndexSender implements ISender{
    @Override
    public void send(MySqlRowData rowData) {
        String level = rowData.getLevel();

        if(DataLevel.LEVEL2.getLevel().equals(level)) {
            Level2RowData(rowData);
        }
        else if (DataLevel.LEVEL3.getLevel().equals(level)){
            Level3RowData(rowData);
        }

        else if (DataLevel.LEVEL4.getLevel().equals(level)) {
            Level4RowData(rowData);
        }

        else {
            log.error("Error found at MySqlRowData: {}", JSON.toJSONString(rowData));
        }

    }

    private void Level2RowData(MySqlRowData rowData){
        if (rowData.getTableName().equals(Constant.AD_PLAN_TABLE_INFO.TABLE_NAME)) {
            List<AdPlanTable> planTables = new ArrayList<>();

            for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {
                AdPlanTable planTable = new AdPlanTable();
                fieldValueMap.forEach((k, v) -> {
                    switch (k){
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_ID:
                            planTable.setId(Long.valueOf(v));
                            break;

                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_USER_ID:
                            planTable.setUserId(Long.valueOf(v));
                            break;

                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_PLAN_STATUS:
                            planTable.setPlanStatus(Integer.valueOf(v));
                            break;

                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_START_DATE:
                            planTable.setStartDate(CommonUtils.parseDate(v));
                            break;

                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_END_DATE:
                            planTable.setEndDate(CommonUtils.parseDate(v));
                            break;
                    }
                });

                planTables.add(planTable);

            }
        }
    }

    private void Level3RowData(MySqlRowData rowData){

    }

    private void Level4RowData(MySqlRowData rowData){

    }
}
