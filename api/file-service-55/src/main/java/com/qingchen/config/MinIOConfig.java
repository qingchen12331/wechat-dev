package com.qingchen.config;

import com.qingchen.utils.MinIOUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIOConfig {

    private static String endpoint;
    private static String fileHost;
    private static String bucketName;
    private static String accessKey;
    private static String secretKey;

    @Value("${minio.endpoint}")
    public void setEndpoint(String endpoint) {
        MinIOConfig.endpoint = endpoint;
    }

    @Value("${minio.fileHost}")
    public void setFileHost(String fileHost) {
        MinIOConfig.fileHost = fileHost;
    }

    @Value("${minio.bucketName}")
    public void setBucketName(String bucketName) {
        MinIOConfig.bucketName = bucketName;
    }

    @Value("${minio.accessKey}")
    public void setAccessKey(String accessKey) {
        MinIOConfig.accessKey = accessKey;
    }

    @Value("${minio.secretKey}")
    public void setSecretKey(String secretKey) {
        MinIOConfig.secretKey = secretKey;
    }

    public static String getBucketName() {
        return bucketName;
    }

    public static String getFileHost() {
        return fileHost;
    }

    @Bean
    public MinIOUtils creatMinioClient() {
        return new MinIOUtils(endpoint, fileHost, bucketName, accessKey, secretKey);
    }
}
