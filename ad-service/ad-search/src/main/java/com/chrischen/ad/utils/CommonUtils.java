package com.chrischen.ad.utils;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Chris Chen
 */
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
}
