package com.qingchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingchen.api.feign.FileMicroServiceFeign;
import com.qingchen.enums.Sex;
import com.qingchen.mapper.UsersMapper;
import com.qingchen.pojo.Users;
import com.qingchen.service.IUsersService;
import com.qingchen.utils.DesensitizationUtil;
import com.qingchen.utils.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author qingchen
 * @since 2025-02-10
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    @Autowired
    private UsersMapper usersMapper;

    private static String USER_FACE1="https://c-ssl.dtstatic.com/uploads/blog/202304/11/20230411004111_3b05d.thumb.1000_0.jpeg";
    @Transactional
    @Override
    public Users createUsers(String mobile, String nickname) {
        Users user=new Users();
        user.setMobile(mobile);
        String uuid= UUID.randomUUID().toString();
        String uuidStr[]=uuid.split("-");
        System.out.println(uuid);
        String wechatNum="wx"+uuidStr[0]+uuidStr[1];
        user.setWechatNum(wechatNum);
        user.setSex(Sex.secret.type);
        user.setFace(USER_FACE1);
        user.setFriendCircleBg(USER_FACE1);
        user.setEmail("");
        user.setBirthday(LocalDateUtils.parseLocalDate("1980-01-01",
                LocalDateUtils.DATE_PATTERN));
        user.setCountry("中国");
        user.setCity("");
        user.setDistrict("");
        user.setProvince("");
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());
        //TODO
//        user.setWechatNumImg("123");
        user.setWechatNumImg(getQrCodeUrl(wechatNum,"temp"));
//        user.setNickname("qingchen");
        if(nickname==null||nickname==""){
            user.setNickname(DesensitizationUtil.commonDisplay(mobile));
        }else {
            user.setNickname(nickname);
        }

        user.setRealName("");
        usersMapper.insert(user);

        return user;
    }

    @Override
    public Users queryMobileIfExist(String mobile) {

        return usersMapper.selectOne(new QueryWrapper<Users>().eq("mobile",mobile));
    }
    @Autowired
    private FileMicroServiceFeign fileMicroServiceFeign;
    private String getQrCodeUrl(String wechatNumber,String userId)  {
        try{
            return fileMicroServiceFeign.generateQrCode(wechatNumber,userId);
        }catch (Exception e){
//        throw new RuntimeException(e);
        }
        return null;

    }
}
