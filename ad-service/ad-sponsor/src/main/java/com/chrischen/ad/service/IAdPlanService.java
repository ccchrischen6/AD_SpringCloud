package com.chrischen.ad.service;

import com.chrischen.ad.entity.AdPlan;
import com.chrischen.ad.exception.AdException;
import com.chrischen.ad.vo.AdPlanGetRequest;
import com.chrischen.ad.vo.AdPlanRequest;
import com.chrischen.ad.vo.AdPlanResponse;

import java.util.List;

/**
 * Created by Chris Chen
 */
public interface IAdPlanService {
    /**
     *
     * @param request
     * @return
     * @throws AdException
     */
    AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException;

    List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException;

    AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException;

    void deleteAdPlan(AdPlanRequest request) throws AdException;
}
