package com.bitcoinj.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class UserCoinAddressEntity {

    private String address;
}
