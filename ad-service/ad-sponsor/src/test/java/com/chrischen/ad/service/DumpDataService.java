package com.chrischen.ad.service;

import com.chrischen.ad.Application;
import com.chrischen.ad.dao.AdPlanRepository;
import com.chrischen.ad.dao.AdUnitRepository;
import com.chrischen.ad.dao.CreativeRepository;
import com.chrischen.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.chrischen.ad.dao.unit_condition.AdUnitItRepository;
import com.chrischen.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.chrischen.ad.dao.unit_condition.CreativeUnitRepostitory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Chris Chen
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DumpDataService {

    @Autowired
    private AdPlanRepository planRepository;
    @Autowired
    private AdUnitRepository unitRepository;
    @Autowired
    private CreativeRepository creativeRepository;
    @Autowired
    private CreativeUnitRepostitory creativeUnitRepostitory;
    @Autowired
    private AdUnitDistrictRepository districtRepository;
    @Autowired
    private AdUnitItRepository itRepository;
    @Autowired
    private AdUnitKeywordRepository keywordRepository;

}
