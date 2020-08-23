package com.chrischen.ad.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by Chris Chen
 */
public class CommonUtils {
    public static String md5(String value) {
        return DigestUtils.md5Hex(value).toUpperCase();
    }
}
