package com.chrischen.ad.constant;

import lombok.Getter;

/**
 * Created by Chris Chen
 */
@Getter
public enum CommonStatus {
    VALID(1, "VALID"),
    INVALID(0, "INVALID");

    private Integer status;
    private String desc;

    CommonStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
