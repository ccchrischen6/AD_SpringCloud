package com.chrischen.ad.search.impl;

import com.chrischen.ad.index.DataTable;
import com.chrischen.ad.index.adUnit.AdUnitIndex;
import com.chrischen.ad.search.ISearch;
import com.chrischen.ad.search.SearchRequest;
import com.chrischen.ad.search.SearchResponse;
import com.chrischen.ad.search.vo.feature.DistrictFeature;
import com.chrischen.ad.search.vo.feature.FeatureRelation;
import com.chrischen.ad.search.vo.feature.ItFeature;
import com.chrischen.ad.search.vo.feature.KeywordFeature;
import com.chrischen.ad.search.vo.media.AdSlot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

        }




        return null;
    }
}
