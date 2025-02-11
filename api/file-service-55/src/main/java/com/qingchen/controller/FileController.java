package com.qingchen.controller;

import ch.qos.logback.classic.Logger;
import com.qingchen.grace.result.GraceJSONResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.channels.MulticastChannel;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
 @PostMapping("uploadFace")
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

}
