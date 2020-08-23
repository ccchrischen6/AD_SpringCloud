package com.chrischen.ad.service.impl;

import com.chrischen.ad.constant.Constants;
import com.chrischen.ad.dao.AdPlanRepository;
import com.chrischen.ad.dao.AdUnitRepository;
import com.chrischen.ad.entity.AdPlan;
import com.chrischen.ad.entity.AdUnit;
import com.chrischen.ad.exception.AdException;
import com.chrischen.ad.service.IAdUnitService;
import com.chrischen.ad.vo.AdUnitRequest;
import com.chrischen.ad.vo.AdUnitResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * Created by Chris Chen
 */
@Service
public class AdUnitServiceImpl implements IAdUnitService {

    private final AdPlanRepository planRepository;
    private final AdUnitRepository unitRepository;

    @Autowired
    public AdUnitServiceImpl(AdPlanRepository planRepository, AdUnitRepository unitRepository) {
        this.planRepository = planRepository;
        this.unitRepository = unitRepository;
    }

    @Override
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdException {
        if(!request.createValidate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        Optional<AdPlan> adPlan = planRepository.findById(request.getPlanId());
        if(!adPlan.isPresent()){
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }

        AdUnit oldAdUnit = unitRepository.findByPlanIdAndUnitName(
                request.getPlanId(),
                request.getUnitName()
        );

        if(oldAdUnit != null){
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
}
