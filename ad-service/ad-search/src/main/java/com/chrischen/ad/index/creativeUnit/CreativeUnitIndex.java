package com.chrischen.ad.index.creativeUnit;

import com.chrischen.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Chris Chen
 */

@Slf4j
@Component
public class CreativeUnitIndex implements IndexAware <String, CreativeUnitObject>{

    // adId-unitId -> {CreativeObject}
    private static Map<String, CreativeUnitObject> objectMap;
    //adId -> {unitId}
    private static Map<Long, Set<Long>> creativeUnitMap;
    //unitId -> {adId}
    private static Map<Long, Set<Long>> unitCreativeMap;

    static {
        objectMap = new ConcurrentHashMap<>();
        creativeUnitMap = new ConcurrentHashMap<>();
        unitCreativeMap = new ConcurrentHashMap<>();
    }

    @Override
    public CreativeUnitObject get(String key) {
        return objectMap.get(key);
    }

    @Override
    public void add(String key, CreativeUnitObject value) {
        log.info("objectMap before adding: {}", objectMap);
        objectMap.put(key,value);

        //add creativeUnitMap
        Set<Long> unitIds = creativeUnitMap.get(value.getAdId());
        if(CollectionUtils.isEmpty(unitIds)){
            unitIds = new ConcurrentSkipListSet<>();
            creativeUnitMap.put(value.getAdId(), unitIds);
        }
        unitIds.add(value.getUnitId());

        //add unitCreativeMap
        Set<Long> adIds = unitCreativeMap.get(value.getUnitId());
        if(CollectionUtils.isEmpty(adIds)){
            adIds = new ConcurrentSkipListSet<>();
            unitCreativeMap.put(value.getUnitId(), adIds);
        }
        adIds.add(value.getAdId());

        log.info("objectMap after adding: {}", objectMap);
    }

    @Override
    public void update(String key, CreativeUnitObject value) {
        log.error("not supported");
    }

    @Override
    public void delete(String key, CreativeUnitObject value) {
        log.info("objectMap before deleting: {}", objectMap);
        objectMap.remove(key);
        Set<Long> unitIds = creativeUnitMap.get(value.getAdId());
        if(CollectionUtils.isNotEmpty(unitIds)){
            unitIds.remove(value.getUnitId());
        }

        Set<Long> adIds = unitCreativeMap.get(value.getUnitId());
        if(CollectionUtils.isNotEmpty(adIds)){
            adIds.remove(value.getAdId());
        }

        log.info("objectMap after deleting: {}", objectMap);

    }


}
