package com.chrischen.ad.consumer;

import com.alibaba.fastjson.JSON;
import com.chrischen.ad.dto.MySqlRowData;
import com.chrischen.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * <h1>consume Binlog message</h1>
 * Created by Chris Chen
 */

@Slf4j
@Component
public class BinlogConsumer {

    private final ISender sender;

    @Autowired
    public BinlogConsumer(ISender sender) {
        this.sender = sender;
    }

    @KafkaListener(topics = {"ad-search-mysql-data"}, groupId = "ad-search")
    public void processMysqlRowData(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        Object message = kafkaMessage.get();
        MySqlRowData rowData = JSON.parseObject(
                message.toString(),
                MySqlRowData.class
        );
        log.info("kafka processMysqlRowData: {}", JSON.toJSONString(rowData));
        sender.send(rowData);
    }
}
