package com.chrischen.ad.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Chris Chen
 */
@Slf4j
public class CommonUtils {
    /**
     * generic
     * if the map contains key, return
     * else
     *  1.create a V typed object (factory.get())
     *  2.put the object to map
     *  3.return the object
     */
    public static <K, V> V getOrCreate(K key, Map<K, V> map, Supplier<V> factory){
        return map.computeIfAbsent(key, k -> factory.get());
    }

    //string concatenation
    public static String stringConcat(String... args) {
        StringBuilder result = new StringBuilder();
        for (String arg : args) {
            result.append(arg);
            result.append("-");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    public static Date parseDate(String dateString) {
        try{
            DateFormat dateFormat = new SimpleDateFormat(
                "EEE MMM dd HH:mm:ss zzz yyyy",
                    Locale.US
            );
            return DateUtils.addHours(
                    dateFormat.parse(dateString),
                    +4
            );
        } catch (ParseException e){
            log.error("parseStringDate error: {}", dateString);
            return null;
        }
    }
}
