package com.chrischen.ad.index.keyword;

import com.chrischen.ad.index.IndexAware;
import com.chrischen.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
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
public class UnitKeywordIndex implements IndexAware<String, Set<Long>> {

    // keyword -> {ids}
    private static Map<String, Set<Long>> keywordUnitMap;
    // id -> {keywords}
    private static Map<Long, Set<String>> unitKeywordMap;

    static {
        keywordUnitMap = new ConcurrentHashMap<>();
        unitKeywordMap = new ConcurrentHashMap<>();
    }


    // get id set by a keyword
    @Override
    public Set<Long> get(String key) {
        if(StringUtils.isEmpty(key)){
            return Collections.emptySet();
        }

        Set<Long> idSet = keywordUnitMap.get(key);
        if(idSet == null){
            return Collections.emptySet();
        }

        return idSet;
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("UnitKeywordIndex, before add: {}", unitKeywordMap);
        log.info("KeywordUnitMap, before add: {}", keywordUnitMap);

        // add ids to given key's id set
        Set<Long> idSet = CommonUtils.getOrCreate(key, keywordUnitMap, ConcurrentSkipListSet::new);
        idSet.addAll(value);

        // add key to every id of given id set
        for(Long id : value){
            Set<String> keywordSet = CommonUtils.getOrCreate(id, unitKeywordMap, ConcurrentSkipListSet::new);
            keywordSet.add(key);
        }

        log.info("UnitKeywordIndex, after add: {}", unitKeywordMap);
        log.info("KeywordUnitMap, after add: {}", keywordUnitMap);


    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("UnitKeywordIndex, before delete: {}", unitKeywordMap);
        log.info("KeywordUnitMap, before delete: {}", keywordUnitMap);

        // delete all ids from given key's ids set
        Set<Long> idSet = CommonUtils.getOrCreate(key, keywordUnitMap, ConcurrentSkipListSet::new);
        idSet.removeAll(value);

        // delete all keys from every id's keySet
        for(Long id : value){
            Set<String> keySet = CommonUtils.getOrCreate(id, unitKeywordMap, ConcurrentSkipListSet::new);
            keySet.remove(id);
        }

        log.info("UnitKeywordIndex, after delete: {}", unitKeywordMap);
        log.info("KeywordUnitMap, after delete: {}", keywordUnitMap);


    }

    @Override
    public void update(String key, Set<Long> value) {
        delete(key, value);
        add(key, value);
    }

    public boolean match (Long unitId, List<String> keywords){
        if(unitKeywordMap.containsKey(unitId) && CollectionUtils.isNotEmpty(unitKeywordMap.get(unitId))){
            Set<String> keywordSet = unitKeywordMap.get(unitId);
            //judge given keywords set is whether a subset of unitId's set
            return CollectionUtils.isSubCollection(keywords, keywordSet);
        }

        else return false;
    }
}










