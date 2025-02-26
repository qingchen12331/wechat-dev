package com.qingchen.test;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinIOTest {
    @Test
    public void testUpload() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //创建客户端
        MinioClient minioClient =MinioClient.builder().endpoint("http://127.0.0.1:9000")
                .credentials("imooc","imooc123456").build();

        String bucketName="localjava";
        //判断当前bucket是否存在
        boolean isExit = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if(!isExit){
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }else{
            System.out.println("当前bucket已经存在");
        }
        //上传本地的文件到minio中
        minioClient.uploadObject(UploadObjectArgs.builder().bucket(bucketName).object("myImage.png")
                .filename("C:\\Users\\27175\\Desktop\\touxiang.jpeg").build());
    }

}
