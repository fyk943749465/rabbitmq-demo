package com.blockcypher.api.pojo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FullTrade extends Balance{


    private Transaction[] txs;
}
