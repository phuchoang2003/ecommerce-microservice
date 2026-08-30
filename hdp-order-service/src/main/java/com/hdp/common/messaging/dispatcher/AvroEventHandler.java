package com.hdp.common.messaging.dispatcher;

import org.apache.avro.specific.SpecificRecord;

public interface AvroEventHandler<T extends SpecificRecord> {
    void handle(T record);
}
