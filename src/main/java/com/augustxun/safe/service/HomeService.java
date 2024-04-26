package com.augustxun.safe.service;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.model.entity.Home;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author augustxun
* @description 针对表【home】的数据库操作Service
* @createDate 2024-04-26 00:04:25
*/
public interface HomeService extends IService<Home> {
    /**
     * 新建一个 Home 贷款账户
     * @param acctNo
     * @return
     */
    public BaseResponse<String> addHomeAccount(Long acctNo);
}