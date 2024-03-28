package com.blockcypher.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BlockCypherService {

    private final RestTemplate restTemplate;

    @Autowired
    public BlockCypherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String getRequest(String url) {

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        HttpStatusCode statusCode = responseEntity.getStatusCode();

        if (statusCode.is2xxSuccessful()) {
            String responseData = responseEntity.getBody();
            return responseData;
        }
        return "error";
    }

    private String postRequest(String url) {

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, null, String.class);

        HttpStatusCode statusCode = responseEntity.getStatusCode();

        if (statusCode.is2xxSuccessful()) {
            String responseData = responseEntity.getBody();
            return responseData;
        }
        return "error";
    }

    public String balance() {

        String url = "https://api.blockcypher.com/v1/btc/main/addrs/1DEP8i3QJCsomS4BSMY2RpU1upv62aGvhD/balance";
        return getRequest(url);
    }


    public String endpoint() {

        String url = "https://api.blockcypher.com/v1/btc/main/addrs/1DEP8i3QJCsomS4BSMY2RpU1upv62aGvhD";

        return getRequest(url);
    }

    public String fullEndpoint() {
        String url = "https://api.blockcypher.com/v1/btc/main/addrs/1DEP8i3QJCsomS4BSMY2RpU1upv62aGvhD/full?before=300000";
        return getRequest(url);
    }

    public String generateKeyPair() {

        String url = "https://api.blockcypher.com/v1/btc/test3/addrs";
        // String url = "https://api.blockcypher.com/v1/btc/test3/addrs?bech32=true";
        return postRequest(url);
    }


    public String chainEndpoint() {

        String url = "https://api.blockcypher.com/v1/btc/main";
        return getRequest(url);
    }

    public String blockHash() {
        String url = "https://api.blockcypher.com/v1/btc/main/blocks/00000000000000000003dc20b868d17121303308f6bba329302e75913f0790db";
        return getRequest(url);
    }

    public String blockHeight() {
        String url = "https://api.blockcypher.com/v1/btc/main/blocks/671142?txstart=1&limit=1";
        return getRequest(url);
    }

    public String feature() {

        // String url = "https://api.blockcypher.com/v1/btc/main/feature/bip65?token=YOURTOKEN";
        String url = "https://api.blockcypher.com/v1/btc/main/feature/bip65?token=08af25e5116f40d3aa43ebd9eaacf81c";
        return getRequest(url);
    }
}
