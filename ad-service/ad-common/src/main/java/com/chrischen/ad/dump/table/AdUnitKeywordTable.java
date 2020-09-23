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
public class AdUnitKeywordTable {
    private Long unitId;
    private String keyword;
}
