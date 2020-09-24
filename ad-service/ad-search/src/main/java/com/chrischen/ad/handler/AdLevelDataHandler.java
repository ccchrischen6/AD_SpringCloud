package com.chrischen.ad.handler;

import com.alibaba.fastjson.JSON;
import com.chrischen.ad.dump.table.*;
import com.chrischen.ad.index.DataTable;
import com.chrischen.ad.index.IndexAware;
import com.chrischen.ad.index.adPlan.AdPlanIndex;
import com.chrischen.ad.index.adPlan.AdPlanObject;
import com.chrischen.ad.index.adUnit.AdUnitIndex;
import com.chrischen.ad.index.adUnit.AdUnitObject;
import com.chrischen.ad.index.creative.CreativeIndex;
import com.chrischen.ad.index.creative.CreativeObject;
import com.chrischen.ad.index.creativeUnit.CreativeUnitIndex;
import com.chrischen.ad.index.creativeUnit.CreativeUnitObject;
import com.chrischen.ad.index.district.UnitDistrictIndex;
import com.chrischen.ad.index.interest.UnitItIndex;
import com.chrischen.ad.index.keyword.UnitKeywordIndex;
import com.chrischen.ad.mysql.constant.OpType;
import com.chrischen.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Chris Chen
 * <p>
 * add, update or delete index
 * <p>
 * indexes of level 2 are separated from other indexes
 *
 * indexes of level 4 depend on indexes of level 3
 */

@Slf4j
public class AdLevelDataHandler {
    private static <K, V> void handleBinLogEvent(
            IndexAware<K, V> indexAware,
            K key,
            V value,
            OpType type) {
        switch (type) {
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

    public static void handleLevel2(AdPlanTable planTable, OpType type) {
        AdPlanObject planObject = new AdPlanObject(
                planTable.getId(),
                planTable.getUserId(),
                planTable.getPlanStatus(),
                planTable.getStartDate(),
                planTable.getEndDate()
        );

        handleBinLogEvent(
                DataTable.of(AdPlanIndex.class),
                planObject.getPlanId(),
                planObject,
                type
        );
    }

    public static void handleLevel2(AdCreativeTable creativeTable, OpType type) {
        CreativeObject creativeObject = new CreativeObject(
                creativeTable.getAdId(),
                creativeTable.getName(),
                creativeTable.getType(),
                creativeTable.getMaterialType(),
                creativeTable.getHeight(),
                creativeTable.getWidth(),
                creativeTable.getAuditStatus(),
                creativeTable.getAdUrl()
        );

        handleBinLogEvent(
                DataTable.of(CreativeIndex.class),
                creativeObject.getAdId(),
                creativeObject,
                type
        );
    }

    public static void handleLevel3(AdUnitTable unitTable, OpType type) {
        AdPlanObject planObject = DataTable.of(AdPlanIndex.class).get(unitTable.getPlanId());
        if (planObject == null) {
            log.error("error found in handleLevel 3, the planObject has not generated: {}",
                    unitTable.getPlanId());
            return;
        }

        AdUnitObject unitObject = new AdUnitObject(
                unitTable.getUnitId(),
                unitTable.getUnitStatus(),
                unitTable.getPositionType(),
                unitTable.getPlanId(),
                planObject
        );

        handleBinLogEvent(
                DataTable.of(AdUnitIndex.class),
                unitTable.getUnitId(),
                unitObject,
                type
        );
    }

    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("creativeUnitTable not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getAdId());

        if (unitObject == null || creativeObject == null) {
            log.error("error found in handleLevel 3, the unitObject or the creativeObject has not generated: {}",
                    JSON.toJSONString(creativeUnitTable));
            return;
        }

        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(
                creativeUnitTable.getAdId(),
                creativeUnitTable.getUnitId()
        );

        handleBinLogEvent(
                DataTable.of(CreativeUnitIndex.class),
                CommonUtils.stringConcat(
                        creativeUnitObject.getAdId().toString(),
                        creativeUnitObject.getUnitId().toString()
                ),
                creativeUnitObject,
                type
        );


    }

    public static void handleLevel4(AdUnitDistrictTable unitDistrictTable, OpType type) {
        if (type == OpType.UPDATE){
            log.error("district index does not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitDistrictTable.getUnitId());

        if(unitObject == null) {
            log.error("error found in handleLevel 4, the unitObject or the creativeObject has not generated: {}",
                    unitDistrictTable.getUnitId());
            return;
        }

        String key = CommonUtils.stringConcat(
                unitDistrictTable.getProvince(),
                unitDistrictTable.getCity());

        Set<Long> value = new HashSet<>(
                Collections.singleton(unitDistrictTable.getUnitId())
        );

        handleBinLogEvent(
                DataTable.of(UnitDistrictIndex.class),
                key,
                value,
                type
        );
    }

    public static void handleLevel4(AdUnitItTable unitItTable, OpType type){
        if(type == OpType.UPDATE){
            log.error("interest index does not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitItTable.getUnitId());
        if(unitObject == null){
            log.error("error found in handleLevel 4, the unitObject has not generated: {}",
                    unitItTable.getUnitId());
            return;
        }

        Set<Long> value = new HashSet<>(
                Collections.singleton(unitItTable.getUnitId())
        );

        handleBinLogEvent(
                DataTable.of(UnitItIndex.class),
                unitItTable.getItTag(),
                value,
                type
        );
    }




    public static void handleLevel4(AdUnitKeywordTable keywordTable, OpType type){
        if(type == OpType.UPDATE){
            log.error("keyword index does not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(keywordTable.getUnitId());
        if(unitObject == null){
            log.error("error found in handleLevel 4, the unitObject or the creativeObject has not generated: {}",
                    keywordTable.getUnitId());
            return;
        }

        Set<Long> value = new HashSet<>(
                Collections.singleton(keywordTable.getUnitId())
        );

        handleBinLogEvent(
                DataTable.of(UnitKeywordIndex.class),
                keywordTable.getKeyword(),
                value,
                type
        );


    }


}








