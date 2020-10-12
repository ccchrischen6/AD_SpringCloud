package com.chrischen.ad.index;

import com.chrischen.ad.utils.CommonUtils;
import lombok.Getter;

/**
 * Created by Chris Chen
 */

@Getter
public enum  CommonStatus {

    VALID(1, "valid status"),
    INVALID(0, "invalid status");

    private Integer status;
    private String desc;

    CommonStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

}
