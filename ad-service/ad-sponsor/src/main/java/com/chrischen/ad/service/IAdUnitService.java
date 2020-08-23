package com.chrischen.ad.service;

import com.chrischen.ad.exception.AdException;
import com.chrischen.ad.vo.AdUnitRequest;
import com.chrischen.ad.vo.AdUnitResponse;

/**
 * Created by Chris Chen
 */
public interface IAdUnitService {
    AdUnitResponse createUnit(AdUnitRequest request) throws AdException;
}
