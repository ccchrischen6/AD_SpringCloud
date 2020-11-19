package com.chrischen.ad.search.impl;

import com.alibaba.fastjson.JSON;
import com.chrischen.ad.index.CommonStatus;
import com.chrischen.ad.index.DataTable;
import com.chrischen.ad.index.adPlan.AdPlanIndex;
import com.chrischen.ad.index.adUnit.AdUnitIndex;
import com.chrischen.ad.index.adUnit.AdUnitObject;
import com.chrischen.ad.index.creative.CreativeIndex;
import com.chrischen.ad.index.creative.CreativeObject;
import com.chrischen.ad.index.creativeUnit.CreativeUnitIndex;
import com.chrischen.ad.index.district.UnitDistrictIndex;
import com.chrischen.ad.index.interest.UnitItIndex;
import com.chrischen.ad.index.keyword.UnitKeywordIndex;
import com.chrischen.ad.search.ISearch;
import com.chrischen.ad.search.vo.SearchRequest;
import com.chrischen.ad.search.vo.SearchResponse;
import com.chrischen.ad.search.vo.feature.DistrictFeature;
import com.chrischen.ad.search.vo.feature.FeatureRelation;
import com.chrischen.ad.search.vo.feature.ItFeature;
import com.chrischen.ad.search.vo.feature.KeywordFeature;
import com.chrischen.ad.search.vo.media.AdSlot;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Chris Chen
 */

@Slf4j
@Service
public class SearchImpl implements ISearch {

    public SearchResponse fallback(SearchRequest request, Throwable e) {
        return null;
    }

    @Override
    @HystrixCommand(fallbackMethod = "fallback")
    public SearchResponse fetchAds(SearchRequest request) {

        // ad position request
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlots();

        //three features
        KeywordFeature keywordFeature = request.getFeatureInfo().getKeywordFeature();
        DistrictFeature districtFeature = request.getFeatureInfo().getDistrictFeature();
        ItFeature itFeature = request.getFeatureInfo().getItFeature();
        FeatureRelation relation = request.getFeatureInfo().getRelation();

        // construct response
        SearchResponse response = new SearchResponse();
        Map<String, List<SearchResponse.Creative>> adSlot2Ads = response.getAdSlot2Ads();

        for (AdSlot adSlot : adSlots) {
            Set<Long> targetUnitIdSet;
            //get initial AdUnit by slot type
            Set<Long> adUnitIdSet = DataTable.of(AdUnitIndex.class).match(adSlot.getPositionType());

            if (relation == FeatureRelation.AND) {
                filterKeywordFeature(adUnitIdSet, keywordFeature);
                filterDistrictFeature(adUnitIdSet, districtFeature);
                filterItTagFeature(adUnitIdSet, itFeature);

                targetUnitIdSet = adUnitIdSet;
            } else {
                targetUnitIdSet = getORRelationUnitIds(
                        adUnitIdSet,
                        keywordFeature,
                        districtFeature,
                        itFeature
                );
            }

            List<AdUnitObject> unitObjects = DataTable.of(AdUnitIndex.class).fetch(targetUnitIdSet);

            filterAdUnitAndPlanStatus(unitObjects, CommonStatus.VALID);
            List<Long> adIds = DataTable.of(CreativeUnitIndex.class).selectAds(unitObjects);
            List<CreativeObject> creatives = DataTable.of(CreativeIndex.class).fetch(adIds);

            //further filter CreativeObject with AdSlot
            filterCreativeByAdSlot(
                    creatives,
                    adSlot.getWidth(),
                    adSlot.getHeight(),
                    adSlot.getType()
            );


            adSlot2Ads.put(adSlot.getAdSlotCode(), buildCreativeResponse(creatives));
        }
        log.info("fetchAds: {}-{}",
                JSON.toJSONString(request),
                JSON.toJSONString(response));

        return response;
    }

    private Set<Long> getORRelationUnitIds(Set<Long> adUnitIdSet,
                                           KeywordFeature keywordFeature,
                                           DistrictFeature districtFeature,
                                           ItFeature itFeature) {
        if (CollectionUtils.isEmpty(adUnitIdSet)) {
            return Collections.emptySet();
        }

        Set<Long> initUnitKeywordSet = new HashSet<>(adUnitIdSet);
        Set<Long> initUnitDistrictSet = new HashSet<>(adUnitIdSet);
        Set<Long> initUnitItSet = new HashSet<>(adUnitIdSet);

        filterKeywordFeature(initUnitKeywordSet, keywordFeature);
        filterDistrictFeature(initUnitDistrictSet, districtFeature);
        filterItTagFeature(initUnitKeywordSet, itFeature);

        return new HashSet<>(
                CollectionUtils.union(
                        initUnitKeywordSet,
                        CollectionUtils.union(
                                initUnitDistrictSet,
                                initUnitItSet)
                )
        );

    }


    private void filterKeywordFeature(Collection<Long> adUnitIds, KeywordFeature keywordFeature) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }

        if (CollectionUtils.isNotEmpty(keywordFeature.getKeywords())) {
            CollectionUtils.filter(
                    adUnitIds,
                    adUnitId ->
                            DataTable.of(UnitKeywordIndex.class).
                                    match(adUnitId, keywordFeature.getKeywords())

            );
        }
    }

    private void filterDistrictFeature(Collection<Long> adUnitIds, DistrictFeature districtFeature) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }

        if (CollectionUtils.isNotEmpty(districtFeature.getDistricts())) {
            CollectionUtils.filter(
                    adUnitIds,
                    adUnitId ->
                            DataTable.of(UnitDistrictIndex.class).match(
                                    adUnitId,
                                    districtFeature.getDistricts()
                            )
            );
        }
    }

    private void filterItTagFeature(Collection<Long> adUnitIds, ItFeature itFeature) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }

        if (CollectionUtils.isNotEmpty(itFeature.getIts())) {
            CollectionUtils.filter(
                    adUnitIds,
                    adUnitId ->
                            DataTable.of(UnitItIndex.class).match(adUnitId, itFeature.getIts())
            );
        }
    }

    private void filterAdUnitAndPlanStatus(List<AdUnitObject> unitObjects, CommonStatus status) {
        if(CollectionUtils.isEmpty(unitObjects)) {
            return;
        }

        CollectionUtils.filter(
                unitObjects,
                object -> object.getUnitStatus().equals(status.getStatus()) &&
                        object.getAdPlanObject().getPlanStatus().equals(status.getStatus())
        );
    }

    private void filterCreativeByAdSlot(
            List<CreativeObject> creatives, Integer width, Integer height, List<Integer> type) {
        if (CollectionUtils.isEmpty(creatives)) {
            return;
        }

        CollectionUtils.filter(
                creatives,
                creative ->
                        creative.getAuditStatus().equals(CommonStatus.VALID.getStatus()) &&
                                creative.getWidth().equals(width) &&
                                creative.getHeight().equals(height) &&
                                type.contains(creative.getType())
        );
    }


    //only get one creative randomly
    private List<SearchResponse.Creative> buildCreativeResponse(List<CreativeObject> creatives) {
        if(CollectionUtils.isEmpty(creatives)) {
            return Collections.emptyList();
        }

        CreativeObject randomObject = creatives.get(Math.abs(new Random().nextInt()) % creatives.size());
        return Collections.singletonList(SearchResponse.convert(randomObject));
    }


}
