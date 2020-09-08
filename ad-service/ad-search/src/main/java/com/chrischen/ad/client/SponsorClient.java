package com.chrischen.ad.client;

import com.chrischen.ad.client.vo.AdPlan;
import com.chrischen.ad.client.vo.AdPlanGetRequest;
import com.chrischen.ad.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by Chris Chen
 */
@FeignClient(value = "eureka-client-ad-sponsor")
public interface SponsorClient {

    @RequestMapping(value = "/ad-sponsor/get/adPlan", method = RequestMethod.POST)
    CommonResponse<List<AdPlan>> getAdPlans(
            @RequestBody AdPlanGetRequest request);
}
