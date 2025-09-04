package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    // 注入阿里云OSS工具类，用于处理文件上传到阿里云OSS
    @Autowired
    private AliOssUtil aliOssUtil;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传:{}",file);
        try {
            // 1. 获取原始文件名
            String originalFilename = file.getOriginalFilename();//原始文件名
            // 2. 截取文件后缀（如.jpg、.png等）
            String extension= originalFilename.substring(originalFilename.lastIndexOf("."));
            // 3. 生成新的文件名（UUID+后缀），避免文件名重复
            String objectName = UUID.randomUUID().toString()+extension;
            // 4. 调用OSS工具类上传文件，获取文件在OSS上的访问路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            // 5. 返回成功响应，包含文件路径
            return Result.success(filePath);
        }catch (IOException e){
            // 捕获文件处理异常，记录错误日志
            log.info("文件上传失败；{}",e);
        }
        // 若出现异常，返回上传失败的错误信息
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
