package com.hdp.order_service.application.handler.getappliedcoupons;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.getappliedcoupons.AppliedCouponView;
import com.hdp.order_service.application.port.in.getappliedcoupons.GetAppliedCouponsQuery;
import com.hdp.order_service.application.port.in.getappliedcoupons.GetAppliedCouponsQueryHandler;
import com.hdp.order_service.application.port.in.getappliedcoupons.GetAppliedCouponsResult;
import com.hdp.order_service.application.port.out.AppliedCouponPersistencePort;
import com.hdp.order_service.application.port.out.OrderPersistencePort;
import com.hdp.order_service.domain.model.AppliedCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAppliedCouponsQueryHandlerImpl implements GetAppliedCouponsQueryHandler {

    private final OrderPersistencePort orderPersistence;
    private final AppliedCouponPersistencePort appliedCouponPersistence;

    @Override
    @Transactional(readOnly = true)
    public GetAppliedCouponsResult handle(GetAppliedCouponsQuery query) {
        orderPersistence.findByIdAndNotDeleted(query.orderId())
            .orElseThrow(() -> new NotFoundException("Order", query.orderId()));
        List<AppliedCoupon> coupons = appliedCouponPersistence.findByOrderId(query.orderId());
        return new GetAppliedCouponsResult(coupons.stream().map(e -> new AppliedCouponView(
            e.getId(), null, e.getCode(),
            e.getCouponType(), e.getDiscountValue()
        )).toList());
    }
}
