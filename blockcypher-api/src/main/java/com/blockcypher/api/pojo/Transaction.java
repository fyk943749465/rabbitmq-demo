package com.blockcypher.api.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Transaction {

    @JsonProperty("block_hash")
    private String blockHash;

    @JsonProperty("block_height")
    private Integer blockHeight;

    @JsonProperty("block_index")
    private Integer blockIndex;

    @JsonProperty("hash")
    private String hash;

    private String[] addresses;

    private Long total;

    private Long fees;

    private Long size;

    private Long vsize;

    private String preference;

    private Date confirmed;

    private Date received;

    private Integer ver;

    @JsonProperty("double_spend")
    private Boolean doubleSpend;

    @JsonProperty("vin_sz")
    private Integer vinSz;

    @JsonProperty("vout_sz")
    private Integer voutSz;

    private Long confirmations;

    private Integer confidence;

    private Input[] inputs;

    private Output[] outputs;
}
