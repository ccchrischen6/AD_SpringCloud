package com.chrischen.ad.index.interest;

import com.chrischen.ad.index.IndexAware;
import com.chrischen.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Chris Chen
 */

@Slf4j
@Component
public class UnitItIndex implements IndexAware<String, Set<Long>> {

    // itTag -> {ids}
    private static Map<String, Set<Long>> itUnitMap;
    // id -> {itTags}
    private static Map<Long, Set<String>> unitItMap;

    static {
        itUnitMap = new ConcurrentHashMap<>();
        unitItMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {
        return itUnitMap.get(key);
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("itUnitMap before add: {}", itUnitMap);
        log.info("unitItMap before add: {}", unitItMap);

        // add ids to given itTag's set
        Set<Long> idSet = CommonUtils.getOrCreate(key, itUnitMap, ConcurrentSkipListSet::new);
        idSet.addAll(value);

        // add itTag to every id
        for(Long id : value){
            Set<String> keywords = CommonUtils.getOrCreate(id, unitItMap, ConcurrentSkipListSet::new);
            keywords.add(key);
        }

        log.info("itUnitMap after add: {}", itUnitMap);
        log.info("unitItMap after add: {}", unitItMap);


    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("itUnitMap before deleting: {}", itUnitMap);
        log.info("unitItMap before deleting: {}", unitItMap);
        // remove all given ids from key's (itTag) set
        Set<Long> ids = CommonUtils.getOrCreate(key, itUnitMap, ConcurrentSkipListSet::new);
        ids.removeAll(value);

        // remove given itTag from every id's itTag set
        for(Long id : value){
            Set<String> itTags = CommonUtils.getOrCreate(id, unitItMap, ConcurrentSkipListSet::new);
            itTags.remove(key);
        }

        log.info("itUnitMap after deleting: {}", itUnitMap);
        log.info("unitItMap after deleting: {}", unitItMap);



    }

    @Override
    public void update(String key, Set<Long> value) {
        delete(key, value);
        add(key, value);
    }

    public boolean match(Long unitId, List<String> itTags) {
        if(unitItMap.containsKey(unitId) && CollectionUtils.isNotEmpty(unitItMap.get(unitId))){
            Set<String> keywords = unitItMap.get(unitId);
            return CollectionUtils.isSubCollection(itTags, keywords);
        }

        return false;
    }


}
