package com.hdp.order_service.application.usecase;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.GetAppliedCouponsUsecase;
import com.hdp.order_service.application.port.out.AppliedCouponPersistencePort;
import com.hdp.order_service.application.port.out.OrderPersistencePort;
import com.hdp.order_service.domain.model.AppliedCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAppliedCouponsUsecaseImpl implements GetAppliedCouponsUsecase {

    private final OrderPersistencePort orderPersistence;
    private final AppliedCouponPersistencePort appliedCouponPersistence;

    @Override
    @Transactional(readOnly = true)
    public Result execute(Query query) {
        orderPersistence.findByIdAndNotDeleted(query.orderId())
            .orElseThrow(() -> new NotFoundException("Order", query.orderId()));
        List<AppliedCoupon> coupons = appliedCouponPersistence.findByOrderId(query.orderId());
        return new Result(coupons.stream().map(e -> new AppliedCouponResult(
            e.getId(), null, e.getCode(),
            e.getCouponType(), e.getDiscountValue()
        )).toList());
    }
}
