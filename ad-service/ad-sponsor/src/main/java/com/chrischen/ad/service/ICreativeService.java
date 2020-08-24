package com.chrischen.ad.service;


import com.chrischen.ad.vo.CreativeRequest;
import com.chrischen.ad.vo.CreativeResponse;

/**
 * Created by Chris Chen
 */
public interface ICreativeService {
    CreativeResponse createCreative(CreativeRequest request);
}
