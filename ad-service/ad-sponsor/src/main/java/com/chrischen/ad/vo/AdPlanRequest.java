package com.chrischen.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Chris Chen
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * this class is used for create, update, and delete request
 */
public class AdPlanRequest {
    /**
     * id is only used for update, when creating a new ad plan, we leave id as empty
     */
    private Long id;
    private Long userId;
    private String planName;
    /**
     * we need to serialize startDate to java Date type
     */
    private String startDate;
    private String endDate;

    public boolean createValidate(){
        return userId != null &&
                !StringUtils.isEmpty(planName) &&
                !StringUtils.isEmpty(startDate) &&
                !StringUtils.isEmpty(endDate);
    }

    public boolean updateValidate() {

        return id != null && userId != null;
    }

    public boolean deleteValidate() {

        return id != null && userId != null;
    }

}
