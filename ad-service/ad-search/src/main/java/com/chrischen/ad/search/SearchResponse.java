package com.chrischen.ad.search;

import com.chrischen.ad.index.creative.CreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Chris Chen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Creative{
        private Long adId;
        private String adUrl;
        private Integer width;
        private Integer height;
        private Integer type;
        private Integer materialType;

        // show monitory url
        private List<String> showMonitorUrl = Arrays.asList(
                "www.chrischen.com",
                "www.chrischen.info"
        );

        // show clickable url
        private List<String> clickMonitorUrl = Arrays.asList(
                "www.chrischen.com",
                "www.chrischen.info"
        );


    }

    public static Creative convert(CreativeObject object){
        Creative creative = new Creative();
        creative.setAdId(object.getAdId());
        creative.setAdUrl(object.getAdUrl());
        creative.setWidth(object.getWidth());
        creative.setHeight(object.getHeight());
        creative.setType(object.getType());
        creative.setMaterialType(object.getMaterialType());

        return creative;
    }

}
