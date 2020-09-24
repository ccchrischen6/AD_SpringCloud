package com.chrischen.ad.handler;

import com.chrischen.ad.dump.table.AdCreativeTable;
import com.chrischen.ad.dump.table.AdPlanTable;
import com.chrischen.ad.dump.table.AdUnitTable;
import com.chrischen.ad.index.DataTable;
import com.chrischen.ad.index.IndexAware;
import com.chrischen.ad.index.adPlan.AdPlanIndex;
import com.chrischen.ad.index.adPlan.AdPlanObject;
import com.chrischen.ad.index.adUnit.AdUnitIndex;
import com.chrischen.ad.index.adUnit.AdUnitObject;
import com.chrischen.ad.index.creative.CreativeIndex;
import com.chrischen.ad.index.creative.CreativeObject;
import com.chrischen.ad.mysql.constant.OpType;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Chris Chen
 *
 * add, update or delete index
 *
 * indexes of level 2 are separated from other indexes
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

    public static void handleLevel2(AdPlanTable planTable, OpType type){
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

    public static void handleLavel2(AdCreativeTable creativeTable, OpType type){
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

    public static void handleLevel3(AdUnitTable unitTable, OpType type){
        AdPlanObject planObject = DataTable.of(AdPlanIndex.class).get(unitTable.getPlanId());
        if(planObject == null){
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
}








