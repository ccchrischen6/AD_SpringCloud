package com.chrischen.ad.index.district;

import com.chrischen.ad.index.IndexAware;
import com.chrischen.ad.search.vo.feature.DistrictFeature;
import com.chrischen.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

/**
 * Created by Chris Chen
 */
@Slf4j
@Component
public class UnitDistrictIndex implements IndexAware<String, Set<Long>> {
    private static Map<String, Set<Long>> districtUnitMap;
    private static Map<Long, Set<String>> unitDistrictMap;

    static {
        districtUnitMap = new ConcurrentHashMap<>();
        unitDistrictMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {
        return districtUnitMap.get(key);
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("unitDistrictMap before adding: {}", unitDistrictMap);
        Set<Long> unitIds = CommonUtils.getOrCreate(key, districtUnitMap, ConcurrentSkipListSet::new);
        unitIds.addAll(value);

        for(Long id : value){
            Set<String> districts = CommonUtils.getOrCreate(id, unitDistrictMap, ConcurrentSkipListSet::new);
            districts.add(key);
        }
        log.info("unitDistrictMap after adding: {}", unitDistrictMap);


    }

    @Override
    public void update(String key, Set<Long> value) {
        log.info("unitDistrictMap before updating: {}", unitDistrictMap);
        log.info("unitDistrictMap after updating: {}", unitDistrictMap);
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("unitDistrict before deleting: {}", unitDistrictMap);
        Set<Long> ids = CommonUtils.getOrCreate(key, districtUnitMap, ConcurrentSkipListSet::new);
        ids.removeAll(value);

        for(Long id : value){
            Set<String> districts = CommonUtils.getOrCreate(id, unitDistrictMap, ConcurrentSkipListSet::new);
            districts.remove(key);
        }

        log.info("unitDistrict after deleting: {}", unitDistrictMap);
    }

    public boolean match(Long adUnitId, List<DistrictFeature.ProvinceAndCity> districts) {
        if(unitDistrictMap.containsKey(adUnitId) &&
                CollectionUtils.isNotEmpty(unitDistrictMap.get(adUnitId))) {
            Set<String> unitDistricts = unitDistrictMap.get(adUnitId);
            List<String> targetDistricts = districts.stream().map(
                    d -> CommonUtils.stringConcat(d.getProvince(), d.getCity())
            ).collect(Collectors.toList());

            return CollectionUtils.isSubCollection(targetDistricts, unitDistricts);
        }

        return false;
    }
}
