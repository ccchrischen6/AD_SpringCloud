package com.chrischen.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Chris Chen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    //device id
    private String deviceCode;

    //mac address
    private String mac;

    private String ip;

    private String model;

    //display resolution
    private String displaySize;

    private String screenSize;

    private String serialName;

}
