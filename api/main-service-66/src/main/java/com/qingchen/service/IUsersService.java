package com.qingchen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qingchen.pojo.Users;
import com.qingchen.pojo.bo.ModifyUserBO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author qingchen
 * @since 2025-02-10
 */
public interface IUsersService extends IService<Users> {
    public void modifyUserInfo(ModifyUserBO userBO);
    public Users getByWechatNumOrMobile(String queryString);
}
