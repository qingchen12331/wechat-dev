package com.qingchen.api.feign;

import com.qingchen.grace.result.GraceJSONResult;
import com.qingchen.pojo.bo.ModifyUserBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "main-service")
public interface UserInfoMicroServiceFeign {
    @PostMapping("/userinfo/updateFace")
    public GraceJSONResult updateFace(@RequestParam("userId") String userId, @RequestParam("face") String face);


}
