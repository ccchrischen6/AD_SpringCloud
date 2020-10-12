package com.chrischen.ad.index.creative;

import com.chrischen.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Chris Chen
 */
@Slf4j
@Component
public class CreativeIndex implements IndexAware<Long, CreativeObject> {
    private static Map<Long, CreativeObject> objectMap;

    static {
        objectMap = new ConcurrentHashMap<>();
    }

    //fetch Creative Object by adIds
    public List<CreativeObject> fetch(Collection<Long> adIds) {
        if(CollectionUtils.isEmpty(adIds)) {
            return Collections.emptyList();
        }

        List<CreativeObject> result = new ArrayList<>();

        adIds.forEach(u -> {
            CreativeObject object = get(u);
            if(null == object) {
                log.error("CreativeObject not found: {}", u);
                return;
            }

            result.add(object);
        });

        return result;
    }

    @Override
    public CreativeObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, CreativeObject value) {
        log.info("creativeObjectMap before adding: {}", objectMap);
        objectMap.put(key, value);
        log.info("creativeObjectMap after adding: {}", objectMap);
    }

    @Override
    public void update(Long key, CreativeObject value) {
        log.info("creativeObjectMap before updating: {}", objectMap);

        CreativeObject oldObject = objectMap.get(key);
        if(oldObject == null){
            objectMap.put(key, value);
        }

        else {
            oldObject.update(value);
        }
        log.info("creativeObjectMap after updating: {}", objectMap);
    }

    @Override
    public void delete(Long key, CreativeObject value) {
        log.info("creativeObjectMap before deleting: {}", objectMap);
        objectMap.remove(key);
        log.info("creativeObjectMap after deleting: {}", objectMap);
    }
}
