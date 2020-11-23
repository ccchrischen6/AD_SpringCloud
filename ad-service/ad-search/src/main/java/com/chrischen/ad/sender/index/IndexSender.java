package com.chrischen.ad.sender.index;

import com.alibaba.fastjson.JSON;
import com.chrischen.ad.dump.table.*;
import com.chrischen.ad.handler.AdLevelDataHandler;
import com.chrischen.ad.index.DataLevel;
import com.chrischen.ad.constant.Constant;
import com.chrischen.ad.dto.MySqlRowData;
import com.chrischen.ad.sender.ISender;
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
@Component
public class IndexSender implements ISender {
    @Override
    public void send(MySqlRowData rowData) {
        String level = rowData.getLevel();

        if (DataLevel.LEVEL2.getLevel().equals(level)) {
            Level2RowData(rowData);
        } else if (DataLevel.LEVEL3.getLevel().equals(level)) {
            Level3RowData(rowData);
        } else if (DataLevel.LEVEL4.getLevel().equals(level)) {
            Level4RowData(rowData);
        } else {
            log.error("Error found at MySqlRowData: {}", JSON.toJSONString(rowData));
        }

    }

    private void Level2RowData(MySqlRowData rowData) {
        if (rowData.getTableName().equals(Constant.AD_PLAN_TABLE_INFO.TABLE_NAME)) {
            List<AdPlanTable> planTables = new ArrayList<>();

            for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {
                AdPlanTable planTable = new AdPlanTable();
                fieldValueMap.forEach((k, v) -> {
                    switch (k) {
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

            planTables.forEach(p -> {
                AdLevelDataHandler.handleLevel2(p, rowData.getOpType());
            });
        } else if (rowData.getTableName().equals(Constant.AD_CREATIVE_TABLE_INFO.TABLE_NAME)) {
            List<AdCreativeTable> creativeTables = new ArrayList<>();
            for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {
                AdCreativeTable creativeTable = new AdCreativeTable();
                fieldValueMap.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_ID:
                            creativeTable.setAdId(Long.valueOf(v));
                            break;

                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_TYPE:
                            creativeTable.setType(Integer.valueOf(v));
                            break;

                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_MATERIAL_TYPE:
                            creativeTable.setMaterialType(Integer.valueOf(v));
                            break;

                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_AUDIT_STATUS:
                            creativeTable.setAuditStatus(Integer.valueOf(v));
                            break;

                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_HEIGHT:
                            creativeTable.setHeight(Integer.valueOf(v));
                            break;

                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_WIDTH:
                            creativeTable.setWidth(Integer.valueOf(v));
                            break;

                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_URL:
                            creativeTable.setAdUrl(v);
                            break;
                    }
                });

                creativeTables.add(creativeTable);
            }

            creativeTables.forEach(c -> {
                AdLevelDataHandler.handleLevel2(c, rowData.getOpType());
            });
        }


    }

    private void Level3RowData(MySqlRowData rowData) {
        if (rowData.getTableName().equals(Constant.AD_UNIT_TABLE_INFO.TABLE_NAME)) {
            List<AdUnitTable> unitTables = new ArrayList<>();
            for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {
                AdUnitTable unitTable = new AdUnitTable();
                fieldValueMap.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_ID:
                            unitTable.setUnitId(Long.valueOf(v));
                            break;

                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_PLAN_ID:
                            unitTable.setPlanId(Long.valueOf(Long.valueOf(v)));
                            break;

                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_UNIT_STATUS:
                            unitTable.setUnitStatus(Integer.valueOf(v));
                            break;

                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_POSITION_TYPE:
                            unitTable.setPositionType(Integer.valueOf(v));
                            break;
                    }
                });

                unitTables.add(unitTable);
            }

            unitTables.forEach(u -> {
                AdLevelDataHandler.handleLevel3(u, rowData.getOpType());
            });
        } else if (rowData.getTableName().equals(Constant.AD_CREATIVE_UNIT_TABLE_INFO.TABLE_NAME)) {
            List<AdCreativeUnitTable> creativeUnitTables = new ArrayList<>();
            for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {
                AdCreativeUnitTable creativeUnitTable = new AdCreativeUnitTable();
                fieldValueMap.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_CREATIVE_ID:
                            creativeUnitTable.setAdId(Long.valueOf(v));
                            break;

                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_UNIT_ID:
                            creativeUnitTable.setUnitId(Long.valueOf(v));
                            break;
                    }
                });
                creativeUnitTables.add(creativeUnitTable);
            }

            creativeUnitTables.forEach(u -> {
                AdLevelDataHandler.handleLevel3(u, rowData.getOpType());
            });
        }
    }

    private void Level4RowData(MySqlRowData rowData) {
        switch (rowData.getTableName()) {
            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.TABLE_NAME:
                List<AdUnitDistrictTable> districtTables = new ArrayList<>();

                for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {

                    AdUnitDistrictTable districtTable = new AdUnitDistrictTable();
                    fieldValueMap.forEach((k, v) -> {
                        switch (k) {
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_UNIT_ID:
                                districtTable.setUnitId(Long.valueOf(v));
                                break;

                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_PROVINCE:
                                districtTable.setProvince(v);
                                break;

                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_CITY:
                                districtTable.setCity(v);
                                break;
                        }
                    });
                    districtTables.add(districtTable);
                }
                districtTables.forEach(d -> {
                    AdLevelDataHandler.handleLevel4(d, rowData.getOpType());
                });
                break;

            case Constant.AD_UNIT_IT_TABLE_INFO.TABLE_NAME:
                List<AdUnitItTable> adUnitItTables = new ArrayList<>();

                for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {

                    AdUnitItTable adUnitItTable = new AdUnitItTable();
                    fieldValueMap.forEach((k, v) -> {
                        switch (k) {
                            case Constant.AD_UNIT_IT_TABLE_INFO.COLUMN_UNIT_ID:
                                adUnitItTable.setUnitId(Long.valueOf(v));
                                break;

                            case Constant.AD_UNIT_IT_TABLE_INFO.COLUMN_IT_TAG:
                                adUnitItTable.setItTag(v);
                                break;
                        }
                    });

                    adUnitItTables.add(adUnitItTable);
                }
                adUnitItTables.forEach(i -> {
                    AdLevelDataHandler.handleLevel4(i, rowData.getOpType());
                });
                break;

            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.TABLE_NAME:
                List<AdUnitKeywordTable> adUnitKeywordTables = new ArrayList<>();
                for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {

                    AdUnitKeywordTable adUnitKeywordTable = new AdUnitKeywordTable();
                    fieldValueMap.forEach((k, v) -> {
                        switch (k) {
                            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_KEYWORD:
                                adUnitKeywordTable.setKeyword(v);
                                break;

                            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_UNIT_ID:
                                adUnitKeywordTable.setUnitId(Long.valueOf(v));
                                break;
                        }
                    });

                    adUnitKeywordTables.add(adUnitKeywordTable);
                }

                adUnitKeywordTables.forEach(k -> {
                    AdLevelDataHandler.handleLevel4(k, rowData.getOpType());
                });
                break;
        }
    }
}



































