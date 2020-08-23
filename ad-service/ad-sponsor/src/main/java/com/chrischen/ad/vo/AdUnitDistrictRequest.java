package com.chrischen.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Chris Chen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdUnitDistrictRequest {

    private List<Long> unitDistricts;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UnitDistrict{
        private Long unitId;
        private String province;
        private String city;
    }
}
