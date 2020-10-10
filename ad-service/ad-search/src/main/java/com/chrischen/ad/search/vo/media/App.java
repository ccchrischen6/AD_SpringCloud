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
public class App {

    private String appCode;

    private String appName;

    private String packageName;

    private String activeName;

}
