package com.chrischen.ad.service.impl;

import com.chrischen.ad.dao.CreativeRepository;
import com.chrischen.ad.entity.Creative;
import com.chrischen.ad.service.ICreativeService;
import com.chrischen.ad.vo.CreativeRequest;
import com.chrischen.ad.vo.CreativeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chris Chen
 */
//it is a java bean
@Service
public class CreativeServiceImpl implements ICreativeService {

    private final CreativeRepository creativeRepository;

    public CreativeServiceImpl(CreativeRepository creativeRepository) {
        this.creativeRepository = creativeRepository;
    }

    @Override
    public CreativeResponse createCreative(CreativeRequest request) {

        Creative creative = creativeRepository.save(request.convertToEntity());
        return new CreativeResponse(creative.getId(), creative.getName());
    }
}
