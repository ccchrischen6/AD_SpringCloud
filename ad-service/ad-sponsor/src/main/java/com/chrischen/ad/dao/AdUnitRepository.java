package com.chrischen.ad.dao;

import com.chrischen.ad.entity.AdUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Chris Chen
 */
public interface AdUnitRepository extends JpaRepository<AdUnit, Long> {
    AdUnit findByPlanIdAndUnitName(Long planId, String unitName);
    List<AdUnit> findAllByUnitStatus(Integer unitStatus);
}
