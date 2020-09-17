package com.chrischen.ad.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by Chris Chen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanTable {

        private Long id;
        private Long userId;
        private Integer planStatus;
        private Date startDate;
        private Date endDate;
}
