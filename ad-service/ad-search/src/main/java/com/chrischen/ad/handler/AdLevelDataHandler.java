package com.chrischen.ad.handler;

import com.alibaba.fastjson.JSON;
import com.chrischen.ad.dump.table.AdCreativeTable;
import com.chrischen.ad.dump.table.AdCreativeUnitTable;
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
import com.chrischen.ad.index.creativeUnit.CreativeUnitIndex;
import com.chrischen.ad.index.creativeUnit.CreativeUnitObject;
import com.chrischen.ad.mysql.constant.OpType;
import com.chrischen.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Chris Chen
 * <p>
 * add, update or delete index
 * <p>
 * indexes of level 2 are separated from other indexes
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

    public static void handleLavel2(AdCreativeTable creativeTable, OpType type) {
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
}








