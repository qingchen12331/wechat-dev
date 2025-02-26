package com.qingchen.api.feign;

import com.qingchen.grace.result.GraceJSONResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "file-service")
public interface FileMicroServiceFeign {
    @PostMapping("/file/generatQrCode")
    public String generateQrCode(@RequestParam("wechatNumber") String wechatNumber,
                                          @RequestParam("userId") String userId) throws Exception;
}
