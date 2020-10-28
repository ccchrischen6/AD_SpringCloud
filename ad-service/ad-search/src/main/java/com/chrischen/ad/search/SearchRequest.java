package com.chrischen.ad.search;

import com.chrischen.ad.search.vo.feature.DistrictFeature;
import com.chrischen.ad.search.vo.feature.FeatureRelation;
import com.chrischen.ad.search.vo.feature.ItFeature;
import com.chrischen.ad.search.vo.feature.KeywordFeature;
import com.chrischen.ad.search.vo.media.AdSlot;
import com.chrischen.ad.search.vo.media.App;
import com.chrischen.ad.search.vo.media.Device;
import com.chrischen.ad.search.vo.media.Geo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Chris Chen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    private String mediaId;

    private RequestInfo requestInfo;

    private FeatureInfo featureInfo;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestInfo {
        private String requestId;

        private List<AdSlot> adSlots;
        private App app;
        private Device device;
        private Geo geo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeatureInfo {
        private KeywordFeature keywordFeature;
        private DistrictFeature districtFeature;
        private ItFeature itFeature;
        private FeatureRelation relation = FeatureRelation.AND;
    }

}
