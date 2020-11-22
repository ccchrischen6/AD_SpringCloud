package com.chrischen.ad.sender.kafka;

import com.alibaba.fastjson.JSON;
import com.chrischen.ad.dto.MySqlRowData;
import com.chrischen.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by Chris Chen
 */
@Component
@Slf4j
public class KafkaSender implements ISender {

    @Value("${adconf.kafka.topic}")
    private String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(MySqlRowData rowData) {
        log.info("ad binlog kafka send rowData......");
        kafkaTemplate.send(topic, JSON.toJSONString(rowData));
    }

}
