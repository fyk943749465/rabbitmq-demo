package com.blockcypher.api.controller;

import com.blockcypher.api.pojo.Balance;
import com.blockcypher.api.pojo.FullTrade;
import com.blockcypher.api.service.BlockCypherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bitcoin")
public class BlockCypherController {

    @Autowired
    private BlockCypherService blockCypherService;

    /**
     * =======================================address start ==================================================
     */

    /**
     * 查询地址余额
     * @param address
     * @return
     */
    @GetMapping("/balance/{address}")
    public Balance getBalance(@PathVariable(required = true) String address) {
        Balance balance = blockCypherService.balance(address);
        return balance;
    }

    /**
     * 查询交易记录
     * @param address
     * @return
     */
    @GetMapping("/fulltrade/{address}/{height}")
    public FullTrade getFullTrade(@PathVariable("address") String address, @PathVariable("height")int height) {

        FullTrade fullTrade = blockCypherService.fullTrade(address, height);
        return fullTrade;
    }

    @GetMapping("/endpoint")
    public String getEndpoint(){
        String endpoint = blockCypherService.endpoint();
        return endpoint;
    }

    @GetMapping("/fullendpoint")
    public String getFullEndpoint() {
        String fullEndpoint = blockCypherService.fullEndpoint();
        return fullEndpoint;
    }

    /**
     * 生成私钥公钥对
     * @return
     */

    @GetMapping("/keypair")
    public String generateKeyPair() {
        String keyPair = blockCypherService.generateKeyPair();
        return keyPair;
    }

    /**
     * =======================================address end ==================================================
     */

    /**
     * ======================================= blockchian start ==================================================
     */

    @GetMapping("/chainendpoint")
    public String getChainEndpoint() {
        return blockCypherService.chainEndpoint();
    }

    @GetMapping("/blockhash")
    public String getBlockHashEndpoint() {
        return blockCypherService.blockHash();
    }

    @GetMapping("/blockheight")
    public String getBlockHeightEndpoint() {
        return blockCypherService.blockHeight();
    }

    @GetMapping("/feature")
    public String getFeature() {
        return blockCypherService.feature();
    }
    /**
     * ======================================= blockchian end ==================================================
     */

}
