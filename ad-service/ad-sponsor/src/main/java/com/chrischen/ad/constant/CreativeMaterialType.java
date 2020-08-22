package com.chrischen.ad.constant;

import lombok.Getter;

/**
 * Created by Chris Chen
 */
@Getter
public enum CreativeMaterialType {
    JPG(1, "JPG"),
    BMP(2, "BMP"),
    MP4(3, "MP4"),
    AVI(4, "AVI"),
    TXT(5, "TXT");

    private int type;
    private String desc;

    CreativeMaterialType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
