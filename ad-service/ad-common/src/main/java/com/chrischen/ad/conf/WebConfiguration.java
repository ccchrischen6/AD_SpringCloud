package com.chrischen.ad.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Created by Chris Chen
 * all req and res will pass the WebConfiguration
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    /**
     *
     * @param converters
     * MappingJackson2HttpMessageConverter converts between java object and JSON
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
