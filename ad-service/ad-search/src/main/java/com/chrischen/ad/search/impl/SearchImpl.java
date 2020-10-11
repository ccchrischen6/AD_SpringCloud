package com.chrischen.ad.search.impl;

import com.chrischen.ad.index.DataTable;
import com.chrischen.ad.index.adUnit.AdUnitIndex;
import com.chrischen.ad.index.district.UnitDistrictIndex;
import com.chrischen.ad.index.interest.UnitItIndex;
import com.chrischen.ad.index.keyword.UnitKeywordIndex;
import com.chrischen.ad.search.ISearch;
import com.chrischen.ad.search.SearchRequest;
import com.chrischen.ad.search.SearchResponse;
import com.chrischen.ad.search.vo.feature.DistrictFeature;
import com.chrischen.ad.search.vo.feature.FeatureRelation;
import com.chrischen.ad.search.vo.feature.ItFeature;
import com.chrischen.ad.search.vo.feature.KeywordFeature;
import com.chrischen.ad.search.vo.media.AdSlot;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Chris Chen
 */

@Slf4j
@Service
public class SearchImpl implements ISearch {
    @Override
    public SearchRequest fetchAds(SearchRequest request) {

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
        }


        return null;
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


}
