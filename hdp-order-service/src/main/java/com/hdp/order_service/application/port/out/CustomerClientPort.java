package com.hdp.order_service.application.port.out;

import java.util.List;
import java.util.UUID;

public interface CustomerClientPort {

    List<UUID> findAddressIdsByBuyerId(UUID buyerId);
}
