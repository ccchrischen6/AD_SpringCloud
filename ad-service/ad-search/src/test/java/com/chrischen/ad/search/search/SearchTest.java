package com.chrischen.ad.search.search;

import com.alibaba.fastjson.JSON;
import com.chrischen.ad.search.Application;
import com.chrischen.ad.search.ISearch;
import com.chrischen.ad.search.vo.SearchRequest;
import com.chrischen.ad.search.vo.feature.DistrictFeature;
import com.chrischen.ad.search.vo.feature.FeatureRelation;
import com.chrischen.ad.search.vo.feature.ItFeature;
import com.chrischen.ad.search.vo.feature.KeywordFeature;
import com.chrischen.ad.search.vo.media.AdSlot;
import com.chrischen.ad.search.vo.media.App;
import com.chrischen.ad.search.vo.media.Device;
import com.chrischen.ad.search.vo.media.Geo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Chris Chen
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SearchTest {

    @Autowired
    private ISearch search;

    @Test
    public void testFetchAds() {
        SearchRequest request = new SearchRequest();
        request.setMediaId("ad-test");

        //build the first test case
        request.setRequestInfo(new SearchRequest.RequestInfo(
                "aaa",
                Collections.singletonList(new AdSlot(
                        "ad-x", 1,
                        1080, 720,
                        Arrays.asList(1, 2),
                        100
                )),
                buildExampleApp(),
                buildExampleGeo(),
                buildExampleDevice()
                ));

        request.setFeatureInfo(buildExampleFeatureInfo(
                Arrays.asList("宝马", "大众"),
                Collections.singletonList(
                        new DistrictFeature.ProvinceAndCity("安徽省", "合肥市")
                ),
                Arrays.asList("台球", "游泳"),
                FeatureRelation.OR
        ));
        System.out.println(JSON.toJSONString(request));
        System.out.println(JSON.toJSONString(search.fetchAds(request)));

        //build the second test case
        request.setRequestInfo(new SearchRequest.RequestInfo(
                "aaa",
                Collections.singletonList(new AdSlot(
                        "ad-y", 1,
                        1080, 720, Arrays.asList(1, 2),
                        1000
                )),
                buildExampleApp(),
                buildExampleGeo(),
                buildExampleDevice()
        ));
        request.setFeatureInfo(buildExampleFeatureInfo(
                Arrays.asList("宝马", "大众", "标志"),
                Collections.singletonList(
                        new DistrictFeature.ProvinceAndCity(
                                "安徽省", "合肥市")),
                Arrays.asList("台球", "游泳"),
                FeatureRelation.AND
        ));
        System.out.println(JSON.toJSONString(request));
        System.out.println(JSON.toJSONString(search.fetchAds(request)));
    }

    private App buildExampleApp() {
        return new App("ad-service", "ad-service",
                "com.chrischen", "video");
    }

    private Geo buildExampleGeo() {
        return new Geo((float) 100, (float) 89, "Beijing", "Beijing");
    }

    private Device buildExampleDevice() {
        return new Device(
                "iphone",
                "1.1.1",
                "127.0.0.1",
                "12",
                "1920 1080",
                "1920 1080",
                "12345678"
        );
    }

    private SearchRequest.FeatureInfo buildExampleFeatureInfo(
            List<String> keywords,
            List<DistrictFeature.ProvinceAndCity> provinceAndCities,
            List<String> its,
            FeatureRelation relation
    ) {
        return new SearchRequest.FeatureInfo(
                new KeywordFeature(keywords),
                new DistrictFeature(provinceAndCities),
                new ItFeature(its),
                relation
        );
    }

}
