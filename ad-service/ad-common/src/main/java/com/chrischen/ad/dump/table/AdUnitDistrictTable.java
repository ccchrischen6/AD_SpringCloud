package com.chrischen.ad.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Chris Chen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitDistrictTable {
    private Long unitId;
    private String province;
    private String city;
}
