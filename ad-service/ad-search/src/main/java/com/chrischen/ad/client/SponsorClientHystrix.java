package com.chrischen.ad.client;

import com.chrischen.ad.client.vo.AdPlan;
import com.chrischen.ad.client.vo.AdPlanGetRequest;
import com.chrischen.ad.vo.CommonResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Chris Chen
 * a fall back service in case SponsorClient is offline
 */
@Component
public class SponsorClientHystrix implements SponsorClient{
    @Override
    public CommonResponse<List<AdPlan>> getAdPlans(AdPlanGetRequest request) {
        return new CommonResponse<>(-1, "eureka-client-ad-sponsor error");
    }
}
