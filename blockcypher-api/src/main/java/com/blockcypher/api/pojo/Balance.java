package com.blockcypher.api.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Balance {

    private String address;

    @JsonProperty("total_received")
    private Long totalReceived;

    @JsonProperty("total_sent")
    private Long totalSent;

    private Long balance;

    @JsonProperty("unconfirmed_balance")
    private Long unconfirmedBalance;

    @JsonProperty("final_balance")
    private Long finalBalance;

    @JsonProperty("n_tx")
    private Integer nTx;

    @JsonProperty("unconfirmed_n_tx")
    private Integer unconfirmedNTx;


    @JsonProperty("final_n_tx")
    private Integer finalNTx;
}
