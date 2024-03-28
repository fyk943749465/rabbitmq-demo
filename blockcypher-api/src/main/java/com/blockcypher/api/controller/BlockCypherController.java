package com.blockcypher.api.controller;

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
    @GetMapping("/balance")
    public String getBalance() {
        String balance = blockCypherService.balance();
        return balance;
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
