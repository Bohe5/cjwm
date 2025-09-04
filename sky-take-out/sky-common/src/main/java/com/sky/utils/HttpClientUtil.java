package com.sky.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http工具类
 */
public class HttpClientUtil {
    // 超时时间：5秒（连接超时、读取超时均使用此配置）
    static final  int TIMEOUT_MSEC = 5 * 1000;

    /**
     * 发送GET方式请求
     * @param url
     * @param paramMap
     * @return
     */
    public static String doGet(String url,Map<String,String> paramMap){
        // 创建Httpclient对象（自动管理连接池）
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 存储响应结果
        String result = "";
        // 响应对象
        CloseableHttpResponse response = null;

        try{
            // 创建URI构建器，用于拼接URL和参数
            URIBuilder builder = new URIBuilder(url);
            // 如果有参数，添加到请求URL中（生成类似?key1=value1&key2=value2的格式）
            if(paramMap != null){
                // 遍历paramMap中所有的键（key）
                for (String key : paramMap.keySet()) {
                    // 向URL构建器添加参数：键为key，值为paramMap中key对应的value
                    builder.addParameter(key,paramMap.get(key));
                }
            }
            // 构建完整的URI（包含参数）
            URI uri = builder.build();

            //创建GET请求
            HttpGet httpGet = new HttpGet(uri);

            //发送请求，获取响应
            response = httpClient.execute(httpGet);

            //判断响应状态，判断响应状态码：200表示请求成功
            if(response.getStatusLine().getStatusCode() == 200){
                // 将响应实体转换为UTF-8编码的字符串
                result = EntityUtils.toString(response.getEntity(),"UTF-8");
            }
        }catch (Exception e){
            // 打印异常堆栈信息（实际项目中建议使用日志框架）
            e.printStackTrace();
        }finally {
            try {
                // 释放资源：关闭响应和HttpClient
                response.close();
                httpClient.close();
            } catch (IOException e) {
                // 打印异常堆栈信息
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 发送POST方式请求
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static String doPost(String url, Map<String, String> paramMap) throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 响应对象
        CloseableHttpResponse response = null;
        // 存储响应结果
        String resultString = "";

        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);

            // 创建参数列表
            // 如果有参数，构造表单实体
            if (paramMap != null) {
                List<NameValuePair> paramList = new ArrayList();
                // 将Map参数转换为NameValuePair列表
                // 遍历paramMap中的每个键值对
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    // 将键值对封装为BasicNameValuePair对象，添加到参数列表
                    paramList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
                // 模拟表单。包装为表单类型实体（默认编码为ISO-8859-1，这里无需额外指定）
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);// 设置请求实体
            }
            // 设置请求超时配置
            httpPost.setConfig(builderRequestConfig());

            // 执行http请求
            response = httpClient.execute(httpPost);
            // 提取响应内容（UTF-8编码）
            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            // 抛出异常给调用者处理
            throw e;
        } finally {
            try {
                // 释放响应资源
                response.close();
            } catch (IOException e) {
                // 打印异常堆栈信息
                e.printStackTrace();
            }
        }

        return resultString;
    }

    /**
     * 发送POST方式请求
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static String doPost4Json(String url, Map<String, String> paramMap) throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 响应对象
        CloseableHttpResponse response = null;
        // 存储响应结果
        String resultString = "";

        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 如果有参数，构造JSON实体
            if (paramMap != null) {
                //构造json格式数据。将Map转换为JSONObject
                JSONObject jsonObject = new JSONObject();
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    // 向JSONObject中添加键值对：键为param.getKey()，值为param.getValue()
                    jsonObject.put(param.getKey(),param.getValue());
                }
                // 创建字符串实体（JSON格式），指定UTF-8编码
                StringEntity entity = new StringEntity(jsonObject.toString(),"utf-8");
                //设置请求编码
                entity.setContentEncoding("utf-8");
                //设置数据类型，设置MIME类型为JSON
                entity.setContentType("application/json");
                // 设置请求实体
                httpPost.setEntity(entity);
            }
            // 设置请求超时配置
            httpPost.setConfig(builderRequestConfig());

            // 执行http请求
            response = httpClient.execute(httpPost);
            // 提取响应内容
            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            // 抛出异常给调用者处理
            throw e;
        } finally {
            try {
                // 释放响应资源
                response.close();
            } catch (IOException e) {
                // 打印异常堆栈信息
                e.printStackTrace();
            }
        }

        return resultString;
    }
    /**
     * 构建请求超时配置
     * @return RequestConfig对象
     */
    private static RequestConfig builderRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(TIMEOUT_MSEC)// 连接超时（建立连接的最长时间）
                .setConnectionRequestTimeout(TIMEOUT_MSEC)// 从连接池获取连接的超时时间
                .setSocketTimeout(TIMEOUT_MSEC).build();// 读取数据超时时间（等待响应的最长时间）
    }

}
