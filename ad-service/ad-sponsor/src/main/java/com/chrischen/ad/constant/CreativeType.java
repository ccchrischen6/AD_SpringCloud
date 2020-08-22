package com.chrischen.ad.constant;

import lombok.Getter;

/**
 * Created by Chris Chen
 */
@Getter
public enum CreativeType {
    IMAGE(1, "IMAGE"),
    VIDEO(2, "VIDEO"),
    TEXT(3, "TEXT");

    private int type;
    private String desc;

    CreativeType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
