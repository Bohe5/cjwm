package com.sky.controller.notify;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.properties.WeChatProperties;
import com.sky.service.OrderService;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * 支付回调相关接口
 */
@RestController
@RequestMapping("/notify")
@Slf4j
public class PayNotifyController {
    // 注入订单服务层，处理支付成功后的业务逻辑（如修改订单状态、发送来单提醒）
    @Autowired
    private OrderService orderService;
    // 注入微信支付配置属性，获取解密所需的API v3密钥
    @Autowired
    private WeChatProperties weChatProperties;

    /**
     * 支付成功回调
     *
     * @param request
     */
    @RequestMapping("/paySuccess")
    public void paySuccessNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取数据
        String body = readData(request);
        log.info("支付成功回调：{}", body);

        //数据解密
        String plainText = decryptData(body);
        log.info("解密后的文本：{}", plainText);
        // 解析解密后的JSON数据，提取关键字段（商户订单号、微信支付交易号）
        JSONObject jsonObject = JSON.parseObject(plainText);
        String outTradeNo = jsonObject.getString("out_trade_no");//商户平台订单号
        String transactionId = jsonObject.getString("transaction_id");//微信支付交易号

        log.info("商户平台订单号：{}", outTradeNo);
        log.info("微信支付交易号：{}", transactionId);

        //业务处理，修改订单状态、来单提醒
        orderService.paySuccess(outTradeNo);

        //给微信响应
        responseToWeixin(response);
    }

    /**
     * 读取数据
     *
     * @param request
     * @return
     * @throws Exception
     */
    private String readData(HttpServletRequest request) throws Exception {
        BufferedReader reader = request.getReader();// 获取请求体阅读器
        StringBuilder result = new StringBuilder();// 拼接读取的每行数据
        String line = null;
        // 逐行读取请求体内容，直至结束
        while ((line = reader.readLine()) != null) {
            if (result.length() > 0) {// 非第一行数据，先添加换行符（保持原始格式）
                result.append("\n");
            }
            result.append(line);
        }
        return result.toString();
    }

    /**
     * 数据解密
     *
     * @param body
     * @return
     * @throws Exception
     */
    private String decryptData(String body) throws Exception {
        // 解析原始加密数据，提取解密所需的核心字段
        JSONObject resultObject = JSON.parseObject(body);
        JSONObject resource = resultObject.getJSONObject("resource");// 加密资源对象
        String ciphertext = resource.getString("ciphertext"); // 加密后的密文
        String nonce = resource.getString("nonce");// 随机串（解密必需，防止重放攻击）
        String associatedData = resource.getString("associated_data");// 附加数据（可选，此处需原样传入）
        // 创建AES解密工具类（需自行实现，遵循微信v3加密规范），传入API v3密钥（从配置中获取）
        AesUtil aesUtil = new AesUtil(weChatProperties.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        //密文解密：参数依次为附加数据字节、随机串字节、密文，返回解密后的明文
        String plainText = aesUtil.decryptToString(associatedData.getBytes(StandardCharsets.UTF_8),
                nonce.getBytes(StandardCharsets.UTF_8),
                ciphertext);

        return plainText;
    }

    /**
     * 给微信响应
     * @param response
     */
    private void responseToWeixin(HttpServletResponse response) throws Exception{
        // 1. 设置响应状态码为200（表示回调已成功处理）
        response.setStatus(200);
        // 2. 构建响应JSON数据（必须包含code和message字段，且code为SUCCESS表示处理成功）
        HashMap<Object, Object> map = new HashMap<>();
        map.put("code", "SUCCESS");
        map.put("message", "SUCCESS");
        // 3. 设置响应内容类型为JSON（微信要求）
        response.setHeader("Content-type", ContentType.APPLICATION_JSON.toString());
        // 4. 将JSON数据写入响应流（指定UTF-8编码，避免中文乱码）
        response.getOutputStream().write(JSONUtils.toJSONString(map).getBytes(StandardCharsets.UTF_8));
        // 5. 强制刷新缓冲区，确保响应立即发送（避免数据滞留）
        response.flushBuffer();
    }
}
