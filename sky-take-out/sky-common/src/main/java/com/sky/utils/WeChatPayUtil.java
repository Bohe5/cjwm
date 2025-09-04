package com.sky.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.properties.WeChatProperties;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * 微信支付工具类
 */
// 注册为 Spring 组件，可通过依赖注入使用
@Component
public class WeChatPayUtil {

    //微信支付下单接口地址
    public static final String JSAPI = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";

    //申请退款接口地址
    public static final String REFUNDS = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds";
    // 注入微信支付配置类（包含 appid、mchid、证书路径等）
    @Autowired
    private WeChatProperties weChatProperties;

    /**
     * 获取调用微信接口的客户端工具对象
     *
     * @return
     */
    private CloseableHttpClient getClient() {
        // 商户 API 私钥（用于签名请求）
        PrivateKey merchantPrivateKey = null;
        try {
            //加载商户 API 私钥（从配置的文件路径读取）
            //merchantPrivateKey商户API私钥，如何加载商户API私钥请看常见问题
            merchantPrivateKey = PemUtil.loadPrivateKey(new FileInputStream(new File(weChatProperties.getPrivateKeyFilePath())));
            //加载平台证书文件（用于验证微信返回结果的签名）
            X509Certificate x509Certificate = PemUtil.loadCertificate(new FileInputStream(new File(weChatProperties.getWeChatPayCertFilePath())));
            //wechatPayCertificates微信支付平台证书列表。你也可以使用后面章节提到的“定时更新平台证书功能”，而不需要关心平台证书的来龙去脉
            List<X509Certificate> wechatPayCertificates = Arrays.asList(x509Certificate);
            //构建微信支付专用 HttpClient（自动处理请求签名、响应验签）
            WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                    // 设置商户信息：商户号、商户证书序列号、商户私钥
                    .withMerchant(weChatProperties.getMchid(), weChatProperties.getMchSerialNo(), merchantPrivateKey)
                    // 设置微信支付平台证书（用于验签）
                    .withWechatPay(wechatPayCertificates);

            // 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签
            CloseableHttpClient httpClient = builder.build();
            return httpClient;
        } catch (FileNotFoundException e) {
            // 捕获证书 / 私钥文件不存在异常
            e.printStackTrace();
            return null;// 实际项目中建议抛出自定义异常，而非返回 null
        }
    }

    /**
     * 发送post方式请求
     *
     * @param url
     * @param body
     * @return
     */
    private String post(String url, String body) throws Exception {
        // 1. 获取配置好的 HttpClient
        CloseableHttpClient httpClient = getClient();
        // 2. 构建 POST 请求
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头：Accept 和 Content-Type 均为 JSON 格式
        httpPost.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        // 设置商户证书序列号（部分接口需要）
        httpPost.addHeader("Wechatpay-Serial", weChatProperties.getMchSerialNo());
        // 设置请求体（JSON 字符串，UTF-8 编码）
        httpPost.setEntity(new StringEntity(body, "UTF-8"));
        // 3. 执行请求并获取响应
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            // 从响应对象中获取响应实体（包含接口返回的内容、编码等信息）,再将响应实体转换为字符串（默认使用响应头指定的编码，若未指定则用ISO-8859-1）
            String bodyAsString = EntityUtils.toString(response.getEntity());
            // 返回转换后的字符串（即接口返回的具体数据）
            return bodyAsString;
        } finally {
            // 4. 释放资源（关闭 HttpClient 和响应）
            httpClient.close();
            response.close();
        }
    }

    /**
     * 发送get方式请求
     *
     * @param url
     * @return
     */
//    与上面发送post方式请求的方法体一样
    private String get(String url) throws Exception {
        CloseableHttpClient httpClient = getClient();
        // 构建 Get 请求
        HttpGet httpGet = new HttpGet(url);
        // 设置请求头（与 POST 一致，符合 API v3 规范）
        httpGet.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
        httpGet.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        httpGet.addHeader("Wechatpay-Serial", weChatProperties.getMchSerialNo());
        // 执行请求并获取响应
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            // 从响应对象中获取响应实体（包含接口返回的内容、编码等信息）,再将响应实体转换为字符串（默认使用响应头指定的编码，若未指定则用ISO-8859-1）
            String bodyAsString = EntityUtils.toString(response.getEntity());
            // 返回转换后的字符串（即接口返回的具体数据）
            return bodyAsString;
        } finally {
            httpClient.close();
            response.close();
        }
    }

    /**
     * jsapi下单
     *
     * @param orderNum    商户订单号
     * @param total       总金额
     * @param description 商品描述
     * @param openid      微信用户的openid
     * @return
     */
    private String jsapi(String orderNum, BigDecimal total, String description, String openid) throws Exception {
        // 1. 构建 JSAPI 下单请求体（JSON 格式）
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appid", weChatProperties.getAppid());// 小程序 appid（从配置获取）
        jsonObject.put("mchid", weChatProperties.getMchid());// 商户号（从配置获取）
        jsonObject.put("description", description);// 商品描述
        jsonObject.put("out_trade_no", orderNum);// 商户订单号
        jsonObject.put("notify_url", weChatProperties.getNotifyUrl());// 支付成功回调地址（从配置获取）
        // 2. 构建金额对象（微信支付金额单位：分，需将元转换为分）
        JSONObject amount = new JSONObject();
        // 元转分：乘以 100，保留 2 位小数后转整数
        // 总金额（分）
        amount.put("total", total.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
        // 货币类型（人民币）
        amount.put("currency", "CNY");

        jsonObject.put("amount", amount);
        // 3. 构建支付者对象（小程序支付需传 openid）
        JSONObject payer = new JSONObject();
        payer.put("openid", openid);

        jsonObject.put("payer", payer);
        // 4. 发送 POST 请求调用 JSAPI 下单接口
        String body = jsonObject.toJSONString();
        return post(JSAPI, body);
    }

    /**
     * 小程序支付
     *
     * @param orderNum    商户订单号
     * @param total       金额，单位 元
     * @param description 商品描述
     * @param openid      微信用户的openid
     * @return
     */
    public JSONObject pay(String orderNum, BigDecimal total, String description, String openid) throws Exception {
        //统一下单，生成预支付交易单（prepay_id）
        String bodyAsString = jsapi(orderNum, total, description, openid);
        //解析返回结果
        JSONObject jsonObject = JSON.parseObject(bodyAsString);
        System.out.println(jsonObject);
        // 提取 prepay_id（预支付交易单 ID，调起支付的核心参数）
        String prepayId = jsonObject.getString("prepay_id");
        if (prepayId != null) {
            // 二次签名（小程序调起支付需用 appid 重新签名，而非商户号）
            // 1.生成签名所需参数
            String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);// 时间戳（秒级）
            String nonceStr = RandomStringUtils.randomNumeric(32);// 随机字符串（32 位数字）
            // 2.构建签名消息（API v3 规范：参数按顺序拼接，末尾加换行）
            ArrayList<Object> list = new ArrayList<>();
            list.add(weChatProperties.getAppid()); // 第 1 个参数：小程序 appid
            list.add(timeStamp);// 第 2 个参数：时间戳
            list.add(nonceStr);// 第 3 个参数：随机字符串
            list.add("prepay_id=" + prepayId); //第 4 个参数：package 参数（固定格式）
            //二次签名，调起支付需要重新签名
            // 拼接签名消息（每个参数后加换行）
            StringBuilder stringBuilder = new StringBuilder();
            for (Object o : list) {
                stringBuilder.append(o).append("\n");
            }
            // 3.使用商户私钥进行 SHA256withRSA 签名
            // 将拼接好的签名消息（StringBuilder）转为字符串
            String signMessage = stringBuilder.toString();
            // 将签名消息字符串转为字节数组（签名算法操作的是字节流）
            byte[] message = signMessage.getBytes();
            // 创建SHA256withRSA签名实例（指定签名算法）
            Signature signature = Signature.getInstance("SHA256withRSA");
            // 初始化签名器：加载商户私钥，准备签名
            signature.initSign(PemUtil.loadPrivateKey(new FileInputStream(new File(weChatProperties.getPrivateKeyFilePath()))));
            // 将待签名的字节数组传入签名器（更新签名内容）
            signature.update(message);
            // 执行签名，将签名结果（字节数组）Base64编码为字符串（便于传输）
            String packageSign = Base64.getEncoder().encodeToString(signature.sign());
            // 4.构建小程序调起支付的参数
            //构造数据给微信小程序，用于调起微信支付
            JSONObject jo = new JSONObject();
            jo.put("timeStamp", timeStamp);// 时间戳
            jo.put("nonceStr", nonceStr);// 随机字符串
            jo.put("package", "prepay_id=" + prepayId);//package（含 prepay_id）
            jo.put("signType", "RSA");// 签名类型（API v3 固定为 RSA）
            jo.put("paySign", packageSign);// 签名类型（API v3 固定为 RSA）

            return jo;
        }
        return jsonObject;
    }

    /**
     * 申请退款
     *
     * @param outTradeNo    商户订单号
     * @param outRefundNo   商户退款单号
     * @param refund        退款金额
     * @param total         原订单金额
     * @return
     */
    public String refund(String outTradeNo, String outRefundNo, BigDecimal refund, BigDecimal total) throws Exception {
        // 1. 构建退款请求体（JSON 格式）
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("out_trade_no", outTradeNo); // 原商户订单号
        jsonObject.put("out_refund_no", outRefundNo);// 商户退款单号
        // 2. 构建金额对象（单位：分，需将元转换为分）
        JSONObject amount = new JSONObject();
        // 退款金额（元转分）
        amount.put("refund", refund.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
        // 原订单金额（元转分）
        amount.put("total", total.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
        amount.put("currency", "CNY");// 货币类型
        // 将金额子对象添加到顶层请求体
        jsonObject.put("amount", amount);
        // 退款成功回调地址（从配置获取）
        jsonObject.put("notify_url", weChatProperties.getRefundNotifyUrl());
        // 3. 发送 POST 请求调用退款接口
        String body = jsonObject.toJSONString();

        //调用申请退款接口
        return post(REFUNDS, body);
    }
}
