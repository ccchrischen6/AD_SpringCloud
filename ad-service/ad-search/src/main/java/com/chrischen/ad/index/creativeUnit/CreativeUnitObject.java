package com.chrischen.ad.index.creativeUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Chris Chen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreativeUnitObject {
    private Long adId;
    private Long unitId;

    //concatenate adId-unitId
}

