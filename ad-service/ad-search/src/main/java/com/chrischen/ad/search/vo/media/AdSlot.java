package com.chrischen.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Chris Chen
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdSlot {
    //ad code
    private String adSlotCode;

    private Integer positionType;

    private Integer width;
    private Integer height;

    //ad type
    private List<Integer> type;

    //min price
    private Integer minCpm;
}
