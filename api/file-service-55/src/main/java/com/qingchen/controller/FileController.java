package com.qingchen.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.qingchen.api.feign.UserInfoMicroServiceFeign;
import com.qingchen.config.MinIOConfig;
import com.qingchen.grace.result.GraceJSONResult;
import com.qingchen.grace.result.ResponseStatusEnum;
import com.qingchen.pojo.vo.UsersVO;
import com.qingchen.utils.JsonUtils;
import com.qingchen.utils.MinIOUtils;
import com.qingchen.utils.QrCodeUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
    @Autowired
    private UserInfoMicroServiceFeign userInfoMicroServiceFeign;
 @PostMapping("uploadFace1")
    public GraceJSONResult upload(@RequestParam("file")MultipartFile file,
                                  String userId, HttpServletRequest request) throws IOException {

     String filename=file.getOriginalFilename();
     String suffixName = filename.substring(filename.lastIndexOf("."));
     String newFileName=userId+suffixName;//文件的新名称
     String rootPath="C:\\Users\\27175\\Desktop\\git_test\\temp"+ File.separator;//上传文件的存放位置
     String filePath=rootPath+File.separator+"face"+File.separator+newFileName;
     File newFile=new File(filePath);
     if(!newFile.getParentFile().exists()){
         newFile.getParentFile().mkdirs();
     }
     file.transferTo(newFile);

    return GraceJSONResult.ok();
 }
    @PostMapping("uploadFace")
    public GraceJSONResult uploadFace(@RequestParam("file")MultipartFile file,
                                  String userId, HttpServletRequest request) throws Exception {

     if(StringUtils.isBlank(userId)){
         return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);

     }

        String filename=file.getOriginalFilename();
        if(StringUtils.isBlank(filename)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        filename="face:"+File.separator+userId+File.separator+filename;
        MinIOUtils.uploadFile(MinIOConfig.getBucketName(),filename,file.getInputStream());
        String faceUrl=MinIOConfig.getFileHost()+"/"+MinIOConfig.getBucketName()+"/"+filename;
        /**
         * 微服务远程调用更新用户头像
         * 如果前端没有保存按钮可以这么做,如果有保存提交按钮,则在前端可以触发
         *
         */
        GraceJSONResult jsonResult = userInfoMicroServiceFeign.updateFace(userId, faceUrl);
        Object data = jsonResult.getData();
        String json= JsonUtils.objectToJson(data);
        UsersVO usersVO = JsonUtils.jsonToPojo(json, UsersVO.class);
        return GraceJSONResult.ok(usersVO);
    }

    @PostMapping("generatQrCode")
    public String generateQrCode(String wechatNumber,
                                      String userId, HttpServletRequest request) throws Exception {
        Map<String,String>map=new HashMap<>();
        map.put("wechatNumber",wechatNumber);
        map.put("userId",userId);
        String data=JsonUtils.objectToJson(map);
        String qrCodeUrl= QrCodeUtils.generateQRCode(data);
        if (!StringUtils.isBlank(qrCodeUrl)){
            String uuid= UUID.randomUUID().toString();
            String objectname="wechatNumber"+File.separator+userId+File.separator+uuid+".png";
            String imageQrCodeUrl = MinIOUtils.uploadFile(MinIOConfig.getBucketName(), objectname, qrCodeUrl, true);
            return imageQrCodeUrl;
        }

        return null;
    }

}
