package com.chrischen.ad.service;

import com.alibaba.fastjson.JSON;
import com.chrischen.ad.Application;
import com.chrischen.ad.constant.CommonStatus;
import com.chrischen.ad.dao.AdPlanRepository;
import com.chrischen.ad.dao.AdUnitRepository;
import com.chrischen.ad.dao.CreativeRepository;
import com.chrischen.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.chrischen.ad.dao.unit_condition.AdUnitItRepository;
import com.chrischen.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.chrischen.ad.dao.unit_condition.CreativeUnitRepostitory;
import com.chrischen.ad.dump.DConstant;
import com.chrischen.ad.dump.table.*;
import com.chrischen.ad.entity.AdPlan;
import com.chrischen.ad.entity.AdUnit;
import com.chrischen.ad.entity.Creative;
import com.chrischen.ad.entity.unit_condition.AdUnitDistrict;
import com.chrischen.ad.entity.unit_condition.AdUnitIt;
import com.chrischen.ad.entity.unit_condition.AdUnitKeyword;
import com.chrischen.ad.entity.unit_condition.CreativeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris Chen
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Slf4j
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


    @Test
    public void dumpAdTableData(){
        dumpAdPlanTable(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_PLAN));
        dumpAdUnitTable(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT));
        dumpAdCreativeTable(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE));
        dumpAdCreativeUnitTable(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE_UNIT));
        dumpAdUnitDistrictTable(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_DISTRICT));
        dumpAdUnitItTable(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_IT));
        dumpAdUnitKeywordTable(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_KEYWORD));

    }

    //save adPlanTable to a local file
    private void dumpAdPlanTable(String fileName){
        List<AdPlan> adPlans = planRepository.findAllByPlanStatus(
                CommonStatus.VALID.getStatus()
        );

        if(CollectionUtils.isEmpty(adPlans)){
            return;
        }

        List<AdPlanTable> planTables = new ArrayList<>();
        adPlans.forEach(p -> planTables.add(
                new AdPlanTable(
                        p.getId(),
                        p.getUserId(),
                        p.getPlanStatus(),
                        p.getStartDate(),
                        p.getEndDate()
                )
        ));

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (AdPlanTable planTable : planTables){
                writer.write(JSON.toJSONString(planTable));
                writer.newLine();
            }
        } catch (IOException e){
            log.error("dumpAdPlanTable error");
        }
    }

    private void dumpAdUnitTable(String fileName){
        List<AdUnit> adUnits = unitRepository.findAllByUnitStatus(
                CommonStatus.VALID.getStatus()
        );

        if(CollectionUtils.isEmpty(adUnits)) {
            return;
        }

        List<AdUnitTable> unitTables = new ArrayList<>();
        adUnits.forEach(p -> unitTables.add(
                new AdUnitTable(
                        p.getId(),
                        p.getUnitStatus(),
                        p.getPositionType(),
                        p.getPlanId()
                )
        ));

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdUnitTable unitTable : unitTables){
                writer.write(JSON.toJSONString(unitTable));
                writer.newLine();
            }
        }
        catch (IOException e){
            log.error("dumpAdUnitTable error");
        }
    }

    private void dumpAdCreativeTable(String fileName){
        List<Creative> creatives = creativeRepository.findAll();
        if(CollectionUtils.isEmpty(creatives)){
            return;
        }

        List<AdCreativeTable> creativeTables = new ArrayList<>();
        creatives.forEach(p -> creativeTables.add(
                new AdCreativeTable(
                        p.getId(),
                        p.getName(),
                        p.getType(),
                        p.getMaterialType(),
                        p.getHeight(),
                        p.getWidth(),
                        p.getAuditStatus(),
                        p.getUrl()
                )
        ));

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdCreativeTable creativeTable: creativeTables){
                writer.write(JSON.toJSONString(creativeTable));
                writer.newLine();
            }
        }
        catch (IOException e){
            log.error("dumpAdCreativeTable error");
        }
    }

    private void dumpAdCreativeUnitTable(String fileName){
        List<CreativeUnit> creativeUnits = creativeUnitRepostitory.findAll();
        if(CollectionUtils.isEmpty(creativeUnits)){
            return;
        }

        List<AdCreativeUnitTable> creativeUnitTables = new ArrayList<>();
        creativeUnits.forEach(c -> creativeUnitTables.add(
                new AdCreativeUnitTable(
                        c.getId(),
                        c.getUnitId()
                )
        ));

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdCreativeUnitTable creativeUnitTable: creativeUnitTables){
                writer.write(JSON.toJSONString(creativeUnitTable));
                writer.newLine();
            }
        }
        catch (IOException e){
            log.error("dumpAdCreativeUnitTable error");
        }
    }

    private void dumpAdUnitDistrictTable(String fileName) {

        List<AdUnitDistrict> unitDistricts = districtRepository.findAll();
        if (CollectionUtils.isEmpty(unitDistricts)) {
            return;
        }

        List<AdUnitDistrictTable> unitDistrictTables = new ArrayList<>();
        unitDistricts.forEach(d -> unitDistrictTables.add(
                new AdUnitDistrictTable(
                        d.getUnitId(),
                        d.getProvince(),
                        d.getCity()
                )
        ));

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdUnitDistrictTable unitDistrictTable : unitDistrictTables) {
                writer.write(JSON.toJSONString(unitDistrictTable));
                writer.newLine();
            }
            writer.close();
        } catch (IOException ex) {
            log.error("dumpAdUnitDistrictTable error");
        }
    }

    private void dumpAdUnitItTable(String fileName){
        List<AdUnitIt> unitIts = itRepository.findAll();
        if(CollectionUtils.isEmpty(unitIts)){
            return;
        }

        List<AdUnitItTable> unitItTables = new ArrayList<>();
        unitIts.forEach(u -> unitItTables.add(
                new AdUnitItTable(
                        u.getUnitId(),
                        u.getItTag()
                )
        ));

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdUnitItTable unitTable: unitItTables){
                writer.write(JSON.toJSONString(unitTable));
                writer.newLine();
            }
        }
        catch (IOException e){
            log.error("dumpAdUnitItTable error");
        }
    }

    private void dumpAdUnitKeywordTable(String fileName){
        List<AdUnitKeyword> unitKeywords = keywordRepository.findAll();
        if(CollectionUtils.isEmpty(unitKeywords)){
            return;
        }

        List<AdUnitKeywordTable> unitKeywordTables = new ArrayList<>();
        unitKeywords.forEach(k -> unitKeywordTables.add(
                new AdUnitKeywordTable(
                        k.getUnitId(),
                        k.getKeyword()
                )
        ));

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdUnitKeywordTable unitKeywordTable: unitKeywordTables){
                writer.write(JSON.toJSONString(unitKeywordTable));
                writer.newLine();
            }
        }
        catch (IOException e){
            log.error("dumpAdUnitKeywordTable error");
        }


    }

}
