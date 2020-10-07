package com.chrischen.ad.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Chris Chen
 */

@Component
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "adconf.mysql")
//construct object from application.yml file
public class BinlogConfig {
    private String host;
    private Integer port;
    private String username;
    private String password;

    private String binlogName;
    private Long position;

}
