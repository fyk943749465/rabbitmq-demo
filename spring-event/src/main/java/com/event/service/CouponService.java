package com.event.service;


import com.event.event.UserRegisterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CouponService {

    @EventListener
    public void addCoupon(UserRegisterEvent event) {
        log.info("给用户[{}]发放优惠券", event.getUsername());
    }
}
