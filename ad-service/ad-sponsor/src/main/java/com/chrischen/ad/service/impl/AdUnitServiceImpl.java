package com.chrischen.ad.service.impl;

import com.chrischen.ad.constant.Constants;
import com.chrischen.ad.dao.AdPlanRepository;
import com.chrischen.ad.dao.AdUnitRepository;
import com.chrischen.ad.dao.CreativeRepository;
import com.chrischen.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.chrischen.ad.dao.unit_condition.AdUnitItRepository;
import com.chrischen.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.chrischen.ad.dao.unit_condition.CreativeUnitRepostitory;
import com.chrischen.ad.entity.AdPlan;
import com.chrischen.ad.entity.AdUnit;
import com.chrischen.ad.entity.unit_condition.AdUnitDistrict;
import com.chrischen.ad.entity.unit_condition.AdUnitKeyword;
import com.chrischen.ad.entity.unit_condition.AdUnitIt;
import com.chrischen.ad.entity.unit_condition.CreativeUnit;
import com.chrischen.ad.exception.AdException;
import com.chrischen.ad.service.IAdUnitService;
import com.chrischen.ad.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Chris Chen
 */
@Service
public class AdUnitServiceImpl implements IAdUnitService {

    private final AdPlanRepository planRepository;
    private final AdUnitRepository unitRepository;

    private final AdUnitKeywordRepository unitKeywordRepository;
    private final AdUnitItRepository unitItRepository;
    private final AdUnitDistrictRepository unitDistrictRepository;

    private final CreativeUnitRepostitory creativeUnitRepostitory;
    private final CreativeRepository creativeRepository;

    @Autowired
    public AdUnitServiceImpl(AdPlanRepository planRepository, AdUnitRepository unitRepository, AdUnitKeywordRepository unitKeywordRepository, AdUnitItRepository unitItRepository, AdUnitDistrictRepository unitDistrictRepository, CreativeRepository creativeRepository, CreativeUnitRepostitory creativeUnitRepostitory) {
        this.planRepository = planRepository;
        this.unitRepository = unitRepository;
        this.unitKeywordRepository = unitKeywordRepository;
        this.unitItRepository = unitItRepository;
        this.unitDistrictRepository = unitDistrictRepository;
        this.creativeRepository = creativeRepository;
        this.creativeUnitRepostitory = creativeUnitRepostitory;
    }

    @Override
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdException {
        if (!request.createValidate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        Optional<AdPlan> adPlan = planRepository.findById(request.getPlanId());
        if (!adPlan.isPresent()) {
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }

        AdUnit oldAdUnit = unitRepository.findByPlanIdAndUnitName(
                request.getPlanId(),
                request.getUnitName()
        );

        if (oldAdUnit != null) {
            throw new AdException(Constants.ErrorMsg.SAME_NAME_UNIT_ERROR);
        }

        AdUnit newAdUnit = unitRepository.save(
                new AdUnit(request.getPlanId(),
                        request.getUnitName(),
                        request.getPositionType(),
                        request.getBudget())
        );

        return new AdUnitResponse(newAdUnit.getId(), newAdUnit.getUnitName());
    }

    @Override
    public AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException {
        //get unitIds from request params
        List<Long> unitIds = request.getUnitKeywords().stream().
                map(AdUnitKeywordRequest.UnitKeyword::getUnitId).collect(Collectors.toList());

        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }


        List<AdUnitKeyword> unitKeywords = new ArrayList<>();

        //add unitKeyword to the list using Java 8
        request.getUnitKeywords().forEach(i -> unitKeywords.add(
                new AdUnitKeyword(i.getUnitId(), i.getKeyword())
        ));

        //write to database and get the ids list
        List<Long> ids = unitKeywordRepository.saveAll(unitKeywords).stream().map(AdUnitKeyword::getId).
                collect(Collectors.toList());

        return new AdUnitKeywordResponse(ids);
    }

    @Override
    public AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException {
        List<Long> unitIds = request.getUnitIts().stream().
                map(AdUnitItRequest.UnitIt::getUnitId).collect(Collectors.toList());

        if(!isRelatedUnitExist(unitIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<AdUnitIt> unitIts = new ArrayList<>();
        request.getUnitIts().forEach(i -> unitIts.add(
                new AdUnitIt(i.getUnitId(), i.getItTag())
        ));

        List<Long> ids = unitItRepository.saveAll(unitIts).stream().
                map(AdUnitIt::getId).collect(Collectors.toList());

        return new AdUnitItResponse(ids);
    }

    @Override
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException {

        List<Long> unitIds = request.getUnitDistricts().stream().
                map(AdUnitDistrictRequest.UnitDistrict::getUnitId).collect(Collectors.toList());

        if(!isRelatedUnitExist(unitIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<AdUnitDistrict> unitDistricts = new ArrayList<>();
        request.getUnitDistricts().forEach(d -> unitDistricts.add(
                new AdUnitDistrict(d.getUnitId(), d.getProvince(), d.getCity())
        ));

        List<Long> ids = unitDistrictRepository.saveAll(unitDistricts).stream().
                map(AdUnitDistrict::getId).collect(Collectors.toList());

        return new AdUnitDistrictResponse(ids);
    }

    @Override
    public CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException {
        List<Long> unitIds = request.getUnitItems().stream().
                map(CreativeUnitRequest.CreativeUnitItem::getUnitId).collect(Collectors.toList());

        List<Long> creativeIds = request.getUnitItems().stream().
                map(CreativeUnitRequest.CreativeUnitItem::getCreativeId).collect(Collectors.toList());

        if(!isRelatedUnitExist(unitIds) || !isRelatedCreativeExist(creativeIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<CreativeUnit> creativeUnits = new ArrayList<>();
        request.getUnitItems().forEach(item -> creativeUnits.add(
                new CreativeUnit(item.getCreativeId(), item.getUnitId())
        ));

        List<Long> ids = creativeUnitRepostitory.saveAll(creativeUnits).stream().
                map(CreativeUnit::getId).collect(Collectors.toList());
        return new CreativeUnitResponse(ids);
    }

    //judge if related units exist
    private boolean isRelatedUnitExist(List<Long> unitIds) {
        if (unitIds.size() == 0) {
            return false;
        }

        //remove duplication
        return unitRepository.findAllById(unitIds).size() == new HashSet<>(unitIds).size();
    }

    //judge if creative is available
    private boolean isRelatedCreativeExist(List<Long> creativeIds){
        if (creativeIds == null){
            return false;
        }

        return creativeRepository.findAllById(creativeIds).size() == new HashSet<>(creativeIds).size();

    }
}










