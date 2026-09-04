package com.hdp.order_service.infrastructure.adapter.outbound.generator;

import com.hdp.order_service.application.port.out.OrderNumberGenerator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LocalOrderNumberGenerator implements OrderNumberGenerator {

    private static final DateTimeFormatter DATE_PREFIX = DateTimeFormatter.ofPattern("yyMMddHHmm");
    private static final char[] BASE32 = "0123456789ABCDEFGHIJKLMNOPQRSTUV".toCharArray();

    @Override
    public String generate() {
        LocalDateTime now = LocalDateTime.now();
        String datePrefix = now.format(DATE_PREFIX);
        int subSecond = now.getSecond() * 100 + now.getNano() / 10_000_000;
        return datePrefix + encodeBase32(subSecond, 4);
    }

    private static String encodeBase32(int value, int width) {
        char[] buf = new char[width];
        for (int i = width - 1; i >= 0; i--) {
            buf[i] = BASE32[value & 0x1F];
            value >>>= 5;
        }
        return new String(buf);
    }
}
