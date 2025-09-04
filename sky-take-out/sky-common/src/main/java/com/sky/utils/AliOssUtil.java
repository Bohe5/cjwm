package com.sky.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayInputStream;
/**
 * 阿里云OSS工具类
 * 封装了文件上传到阿里云OSS的核心逻辑
 */
// Lombok注解，自动生成getter、setter、toString等方法
@Data
// Lombok注解，生成包含所有参数的构造方法
@AllArgsConstructor
// Lombok注解，提供日志功能，可通过log变量输出日志
@Slf4j
public class AliOssUtil {
    //与之前在AliOssProperties文件配置的一样，但这里是与yml配置文件中的AliOss属性一一对应，从配置属性类中获取已注入好的配置。
    // OSS服务的访问端点（Endpoint）
    private String endpoint;
    // 访问OSS的AccessKey ID
    private String accessKeyId;
    // 访问OSS的AccessKey Secret
    private String accessKeySecret;
    // OSS存储桶（Bucket）名称
    private String bucketName;

    /**
     * 文件上传
     *
     * @param bytes
     * @param objectName
     * @return
     */
    public String upload(byte[] bytes, String objectName) {

        // 创建OSSClient实例。创建OSS客户端实例，用于与OSS服务交互
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 创建PutObject请求。将字节数组上传到指定Bucket和路径
            // 使用ByteArrayInputStream将字节数组转换为输入流
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        } catch (OSSException oe) {
            // 捕获OSS服务端异常（请求已到达OSS，但被拒绝）
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());//打印异常的具体错误描述
            System.out.println("Error Code:" + oe.getErrorCode());//打印阿里云定义的错误码
            System.out.println("Request ID:" + oe.getRequestId());//打印本次请求的唯一标识
            System.out.println("Host ID:" + oe.getHostId());//打印处理该请求的 OSS 服务器主机标识。
        } catch (ClientException ce) {
            // 捕获客户端异常（客户端与OSS通信时出现问题，如网络不可达）
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());//打印异常的具体错误描述
        } finally {
            // 无论上传成功与否，都需要关闭OSS客户端，释放资源
            //shutdown()方法是用于关闭客户端连接、释放底层资源
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        // 构建并返回上传文件的访问URL
        //文件访问路径规则 https://BucketName.Endpoint/ObjectName
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);
        // 记录文件上传成功的URL日志
        log.info("文件上传到:{}", stringBuilder.toString());
        //拼接的URL以字符串的方式返回
        return stringBuilder.toString();
    }
}
