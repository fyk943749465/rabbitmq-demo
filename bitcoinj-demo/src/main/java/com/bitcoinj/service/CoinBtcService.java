package com.bitcoinj.service;


import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class CoinBtcService {

    private  String url = "http://192.168.10.16:18332";
    private  String username = "test";
    private  String password = "test";

    private final static String RESULT = "result";
    private final static String METHOD_SEND_TO_ADDRESS = "sendtoaddress";
    private final static String METHOD_GET_BLOCK = "getblock";
    private final static String METHOD_GET_BLOCK_HASH = "getblockhash";
    private final static String METHOD_GET_TRANSACTION = "gettransaction";
    private final static String METHOD_GET_BLOCK_COUNT = "getblockcount";
    private final static String METHOD_NEW_ADDRESS = "getnewaddress";
    private final static String METHOD_GET_BALANCE = "getbalance";

    private final static String METHOD_GET_BALANCE_BY_ADDRESS = "getreceivedbyaddress";
    private final static int MIN_CONFIRMATION= 6;

    //前四个参数在BTC钱包conf文件中设置
    //钱包密码PASSWORD打开钱包后设置的密码

    /***
     * 取得钱包相关信息
     * 若获取失败，result为空，error信息为错误信息的编码
     * */
    public JSONObject getInfo() throws Exception {
        return doRequest("getinfo");
    }

    /**
     * 获取块链信息
     * @return
     * @throws Exception
     */
    public JSONObject getBlockChainInfo() throws Exception {
        return doRequest("getblockchaininfo");
    }


    /**
     * BTC产生地址
     * @return
     */
    public String getNewAddress(){
        JSONObject json = doRequest(METHOD_NEW_ADDRESS);
        if(isError(json)){
            log.error("获取BTC地址失败:{}",json.get("error"));
            return "";
        }
        return json.getString(RESULT);
    }

    /**
     * BTC查询余额
     * @return
     */
    public double getBalance(){
        JSONObject json = doRequest(METHOD_GET_BALANCE);
        if(isError(json)){
            log.error("获取BTC余额:{}",json.get("error"));
            return 0;
        }
        return json.getDouble(RESULT);
    }

    public double getReceivedByAddress(String address){
        JSONObject json = doRequest(METHOD_GET_BALANCE_BY_ADDRESS, address);

        if(isError(json)){
            log.error("获取BTC余额:{}", json.get("error"));
            return 0;
        }
        return json.getDouble(RESULT);
    }

    /**
     * BTC转帐
     * @param addr
     * @param value
     * @return
     */
    public String send(String addr, double value){
        if(vailedAddress(addr)){
            JSONObject json = doRequest(METHOD_SEND_TO_ADDRESS,addr,value);
            if(isError(json)){
                log.error("BTC 转帐给{} value:{}  失败 ：",addr,value,json.get("error"));
                return "";
            }else{
                log.info("BTC 转币给{} value:{} 成功",addr,value);
                return json.getString(RESULT);
            }
        }else{
            log.error("BTC接受地址不正确");
            return "";
        }
    }

    /**
     * 验证地址的有效性
     * @param address
     * @return
     * @throws Exception
     */
    public boolean vailedAddress(String address) {
        JSONObject json  = doRequest("validateaddress",address);
        if(isError(json)){
            log.error("BTC验证地址失败:",json.get("error"));
            return false;
        }else{
            return json.getJSONObject(RESULT).getBoolean("isvalid");
        }
    }


    /**
     * 区块高度
     * @return
     */
    public int getBlockCount(){
        JSONObject json = null;
        try {
            json = doRequest(METHOD_GET_BLOCK_COUNT);
            if(!isError(json)){
                return json.getInteger("result");
            }else{
                log.error(json.toString());
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean parseBlock(int index) {
        JSONObject jsonBlockHash = doRequest(METHOD_GET_BLOCK_HASH, index);
        if (isError(jsonBlockHash)) {
            log.error("访问BTC出错");
            return false;
        }
        String hash = jsonBlockHash.getString(RESULT);
        JSONObject jsonBlock = doRequest(METHOD_GET_BLOCK, hash);
        if (isError(jsonBlock)) {
            log.error("访问BTC出错");
            return false;
        }
        JSONObject jsonBlockResult = jsonBlock.getJSONObject(RESULT);
        int confirm = jsonBlockResult.getInteger("confirmations");
        if (confirm >= MIN_CONFIRMATION) {
            JSONArray jsonArrayTx = jsonBlockResult.getJSONArray("tx");
            if (jsonArrayTx == null || jsonArrayTx.size() == 0) {
                //没有交易
                return true;
            }
            Iterator<Object> iteratorTxs = jsonArrayTx.iterator();
            while(iteratorTxs.hasNext()){
                String txid = (String) iteratorTxs.next();
                parseTx(txid,confirm,null);
            }
            return true;
        }else{
            return false;
        }
    }

    public void parseTx(String txid, int coinfirm, List<UserCoinAddressEntity> userList){
        JSONObject jsonTransaction = doRequest(METHOD_GET_TRANSACTION, txid);
        if(isError(jsonTransaction)) {
            //log.error("处理BTC tx出错");
            return;
        }
        JSONObject jsonTransactionResult = jsonTransaction.getJSONObject(RESULT);
        JSONArray jsonArrayVout = jsonTransactionResult.getJSONArray("details");
        if(jsonArrayVout == null || jsonArrayVout.size() == 0){
            return;
        }
        Iterator<Object> iteratorVout = jsonArrayVout.iterator();
        while (iteratorVout.hasNext()) {
            JSONObject jsonVout = (JSONObject) iteratorVout.next();
            double value = jsonVout.getDouble("amount");
            String category = jsonVout.getString("category");
            if(value >0&&"receive".equals(category)) {
                String address = jsonVout.getString("address");
                for (UserCoinAddressEntity addressModel : userList) {
                    //如果有地址是分配给用记的地址， 则说明用户在充值
                    if (address.equals(addressModel.getAddress())) {
                        //添加充值记录
                        log.info("用户充值");

                    }
                }
            }
        }
    }


    private boolean isError(JSONObject json){
        if( json == null || (StringUtils.isNotEmpty(json.getString("error")) && json.get("error") != "null")){
            return true;
        }
        return false;
    }



    private JSONObject doRequest(String method, Object... params){
        JSONObject param = new JSONObject();
        param.put("id", System.currentTimeMillis()+"");
        param.put("jsonrpc", "2.0");
        param.put("method", method);
        if(params != null){
            param.put("params",params);
        }
        String creb = Base64.encodeBase64String((username + ":" + password).getBytes());
        Map<String,String> headers = new HashMap<>(2);
        headers.put("Authorization","Basic "+creb);
        String resp = "";
        if (METHOD_GET_TRANSACTION.equals(method)){
            try{
                resp = doPostJson(url, param.toJSONString(), headers);
            }catch (Exception e){
                if (e instanceof IOException){
                    resp = "{}";
                }
            }
        }else{
            resp = doPostJson(url,param.toJSONString(), headers);
        }
        return JSON.parseObject(resp);
    }




    public static String doPostJson(String url, String jsonParam, Map<String, String> headers) {
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = wrapClient(url);
        try {
            httpPost = new HttpPost(url);
            //addHeader，如果Header没有定义则添加，已定义则不变，setHeader会重新赋值
            httpPost.addHeader("Content-type","application/json;charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            StringEntity entity = new StringEntity(jsonParam, StandardCharsets.UTF_8);
//            entity.setContentType("text/json");
//            entity.setContentEncoding(new BasicHeader("Content-Type", "application/json;charset=UTF-8"));
            httpPost.setEntity(entity);
            //是否有header
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 执行请求
            response = httpClient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }

        } catch (Exception e) {

           log.error("[发送POST请求错误：]" + e.getMessage());
           throw new RuntimeException(e.getMessage());
        } finally {
            try {
                httpPost.releaseConnection();
                response.close();
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static CloseableHttpClient wrapClient(String url) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        if (url.startsWith("https")) {
            client = getCloseableHttpsClients();
        }
        return client;
    }

    private static CloseableHttpClient getCloseableHttpsClients() {
        // 采用绕过验证的方式处理https请求
        SSLContext sslcontext = createIgnoreVerifySSL();
        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext)).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);
        // 创建自定义的httpsclient对象
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();
        return client;
    }
    private static SSLContext createIgnoreVerifySSL() {
        // 创建套接字对象
        SSLContext sslContext = null;
        try {
            //指定TLS版本
            sslContext = SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("[创建套接字失败:] " + e.getMessage());
        }
        // 实现X509TrustManager接口，用于绕过验证
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        try {
            //初始化sslContext对象
            sslContext.init(null, new TrustManager[]{trustManager}, null);
        } catch (KeyManagementException e) {
            throw new RuntimeException("[初始化套接字失败:] " + e.getMessage());
        }
        return sslContext;
    }


    public static void main(String args[]) throws Exception{
        CoinBtcService btcUtils = new CoinBtcService();

//        System.out.println(btcUtils.getInfo());
//        System.out.println(btcUtils.getBlockChainInfo());
//        System.out.println(btcUtils.getBalance());
        System.out.println(btcUtils.getBlockCount());
//        System.out.println(btcUtils.getNewAddress());
//        System.out.println(btcUtils.getReceivedByAddress("2N5jvDksYvkJijb9iMrNmqwrKHke6KMbLMm"));
    }
}
